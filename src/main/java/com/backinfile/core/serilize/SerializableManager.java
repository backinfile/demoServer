package com.backinfile.core.serilize;

import com.backinfile.support.Log;
import com.backinfile.utils.ReflectionUtils;
import com.google.protobuf.Message;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 代码生成改为反射形式
 */
public class SerializableManager {

	@SuppressWarnings("unchecked")
	public static <T> T parseFromSerializeID(int id) {
		return (T) idSaves.get(id);
	}

//    public static int getMessageID(Message message) {
//        DescriptorProtos.MessageOptions options = message.getDescriptorForType().getOptions();
//        if (options.hasExtension(Msg.msgId)) {
//            return options.getExtension(Msg.msgId);
//        }
//        return -1;
//    }

	public static int getCommonSerializeID(Object obj) {
		if (obj instanceof Class) {
			return ((Class<?>) obj).getName().hashCode();
		}
		return obj.getClass().getName().hashCode();
	}

	private static final Map<Integer, Object> idSaves = new HashMap<>();

	public static void registerAll() {
		registerAllEnum();
		registerAllSerialize();
		registerAllMessage();
	}

	private static void registerAllSerialize() {
		Set<Class<?>> classes = ReflectionUtils.getClassesExtendsClass(ISerializable.class);
		for (Class<?> clazz : classes) {
			try {
				int id = getCommonSerializeID(clazz);
				Constructor<?> constructor = clazz.getDeclaredConstructor();
				idSaves.put(id, constructor);
			} catch (Exception e) {
				Log.Core.error("可能是ISerializable接口的实现没有空的构造函数", e);
			}
		}
	}

	private static void registerAllMessage() {
		Set<Class<?>> classes = ReflectionUtils.getClassesExtendsClass(Message.class);
		for (Class<?> clazz : classes) {
			try {
				int id = getCommonSerializeID(clazz);
				Method method = clazz.getDeclaredMethod("newBuilder");
				Object value = method.invoke(null);
				idSaves.put(id, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void registerAllEnum() {
		Set<Class<?>> classes = ReflectionUtils.getClassesExtendsClass(Enum.class);
		for (Class<?> clazz : classes) {
			try {
				int id = getCommonSerializeID(clazz);
				Method method = clazz.getDeclaredMethod("values");
				Object value = method.invoke(null);
				idSaves.put(id, value);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
