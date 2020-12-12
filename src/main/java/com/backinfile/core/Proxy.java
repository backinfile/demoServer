package com.backinfile.core;

import java.lang.reflect.InvocationTargetException;

import com.backinfile.support.Log;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class Proxy {
	@SuppressWarnings("unchecked")
	public <T extends IService> T createProxy(Class<T> serviceClass) {
		return (T) new TempClass();
	}

	public static class TempClass implements IService {
		@ProxyMethod
		public void publicMethod() {
			Log.Game.info("in publicMethod");
		}
	}

	static void createProxyClass(Class<? extends IService> serviceClass)
			throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ClassPool pool = ClassPool.getDefault();
		CtClass superClass = pool.get(serviceClass.getName());
		CtClass curClass = pool.makeClass(serviceClass.getName() + "$Proxy");
		curClass.setSuperclass(superClass);

		for (CtMethod m : superClass.getMethods()) {
			if (!m.hasAnnotation(ProxyMethod.class)) {
				continue;
			}
			Log.Game.info("rigister method: {}:{}", serviceClass.getSimpleName(), m.getName());

			CtMethod ctMethod = new CtMethod(m.getReturnType(), m.getName(), m.getParameterTypes(), curClass);
			ctMethod.setModifiers(Modifier.PUBLIC);
			ctMethod.setBody("{System.out.println(\"in ctMethod\");}");
			ctMethod.set
			curClass.addMethod(ctMethod);
		}

		Class<?> clazz = curClass.toClass();
		TempClass temp = (TempClass) clazz.getDeclaredConstructor().newInstance();
		Log.Game.info(clazz.getName());
		temp.publicMethod();
	}

	public static void main(String[] args)
			throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		createProxyClass(TempClass.class);
	}
}
