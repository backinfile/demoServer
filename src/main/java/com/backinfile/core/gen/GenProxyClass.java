package com.backinfile.core.gen;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import com.backinfile.core.IService;
import com.backinfile.core.ProxyMethod;
import com.backinfile.core.StandAloneService;
import com.backinfile.utils.ReflectionUtils;

public class GenProxyClass extends GenBase {

	public GenProxyClass() {
		setTemplateFileName("proxy.ftl");
		setTargetPackage("com.backinfile.gen.proxy");
	}

	@Override
	public int genFile() {
		int code = 0;
		var classes = ReflectionUtils.getClassesExtendsClass(IService.class);
		for (var clazz : classes) {
			code = genClass(clazz);
			if (code != 0) {
				break;
			}
		}

		return code;
	}

	private Set<String> imports = new HashSet<>();

	public int genClass(Class<?> clazz) {
		rootMap.clear();
		imports.clear();

		Map<String, Object> map = rootMap;
		map.put("imports", imports);
		map.put("className", clazz.getSimpleName() + "Proxy");
		map.put("classFullName", clazz.getName() + "Proxy");
		map.put("oriClassName", clazz.getSimpleName());
		map.put("oriClassFullName", clazz.getName());
		map.put("standalone", clazz.isAnnotationPresent(StandAloneService.class));
		imports.add(getFullPackageName(clazz));

		List<Map<String, Object>> list = new ArrayList<>();
		map.put("methods", list);

		for (var method : clazz.getMethods()) {
			if (method.getAnnotation(ProxyMethod.class) == null)
				continue;

			Map<String, Object> methodVars = new HashMap<>();
			list.add(methodVars);
			methodVars.put("name", method.getName());

			Parameter[] parameters = method.getParameters();
			// ${arg.typeName} ${arg.name}${arg.dot}
			List<Map<String, Object>> typeArgs = getTypeArgs(parameters);
			methodVars.put("args", typeArgs);
			methodVars.put("argsCount", typeArgs.size());
			methodVars.put("methodKey", String.valueOf(method.toString().hashCode()));
			methodVars.put("isPara", typeArgs.stream().anyMatch(m -> (boolean) m.get("isPara")));
//			imports.addAll(
//					Arrays.stream(parameters).map(t -> getFullPackageName(t.getClass())).collect(Collectors.toList()));

		}

		imports.removeIf(s -> s.isEmpty());
		setFileName(clazz.getSimpleName() + "Proxy.java");
		return super.genFile();
	}

	private List<Map<String, Object>> getTypeArgs(Parameter[] parameters) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];

			Class<?> type = parameter.getType();
			Map<String, Object> map = new HashMap<>();
			list.add(map);
			map.put("typeName", type.getSimpleName());
			map.put("wrapName", ReflectionUtils.wrap(type).getSimpleName());
			map.put("name", parameter.getName());
			map.put("dot", i == parameters.length - 1 ? "" : ", ");
			map.put("isPara", parameter.getParameterizedType() instanceof ParameterizedType);

			imports.add(getFullPackageName(type));
			imports.add(getFullPackageName(ReflectionUtils.wrap(type)));
		}
		return list;
	}

	private String getFullPackageName(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return "";
		}
		if (clazz.isArray()) {
			return getFullPackageName(clazz.getComponentType());
		}
		return clazz.getPackageName() + "." + clazz.getSimpleName();
	}
}
