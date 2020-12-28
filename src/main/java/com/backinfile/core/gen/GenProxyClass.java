package com.backinfile.core.gen;


import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import com.backinfile.core.ProxyMethod;

public class GenProxyClass extends GenBase {

    public GenProxyClass() {
        setTemplateFileName("ProxyClass.ftl");
        setTargetPackage("com.bif.gen.proxy");
    }


    @Override
    public int genFile() {
        int code = 0;
//        var classes = ReflectionUtils.getClassesExtendsClass(StandAloneService.class);
//        for (var clazz : classes) {
//            code = genClass(clazz);
//            if (code != 0) {
//                break;
//            }
//        }

        return code;
    }

    public int genClass(Class<?> clazz) {
        rootMap.clear();
        Set<String> imports = new HashSet<>();

        Map<String, Object> map = rootMap;
        map.put("imports", imports);
        map.put("className", clazz.getSimpleName() + "Proxy");
        map.put("name", clazz.getSimpleName());
        map.put("fullName", clazz.getName() + "Proxy");
        imports.add(getFullPackageName(clazz));

        List<Map<String, Object>> list = new ArrayList<>();
        map.put("methods", list);

        for (var method : clazz.getMethods()) {
            if (method.getAnnotation(ProxyMethod.class) == null) continue;

            Map<String, Object> methodVars = new HashMap<>();
            list.add(methodVars);
            methodVars.put("name", method.getName());

            Parameter[] parameters = method.getParameters();
            // ${arg.typeName} ${arg.name}${arg.dot}
            methodVars.put("args", getTypeArgs(parameters));
            imports.addAll(Arrays.stream(parameters).map(t -> getFullPackageName(t.getClass())).collect(Collectors.toList()));

        }


        setFileName(clazz.getSimpleName() + "Proxy.java");
        return super.genFile();
    }


    private List<Map<String, Object>> getTypeArgs(Parameter[] parameters) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            Map<String, Object> map = new HashMap<>();
            list.add(map);
            map.put("typeName", parameter.getType().getSimpleName());
            map.put("name", parameter.getName());
            map.put("dot", i == parameters.length - 1 ? "" : ",");
        }
        return list;
    }

    private String getFullPackageName(Class<?> clazz) {
        return clazz.getPackageName() + "." + clazz.getSimpleName();
    }
}
