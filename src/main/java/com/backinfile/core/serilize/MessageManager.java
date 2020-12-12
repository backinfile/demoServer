package com.backinfile.core.serilize;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.backinfile.utils.ReflectionUtils;
import com.google.protobuf.Message;

public class MessageManager {
	private static final Map<String, Message.Builder> map = new HashMap<>();

	public static String pack(Message message) {
		byte[] bytes = message.toByteArray();
		String str = new String(bytes, StandardCharsets.UTF_8);
		return message.getClass().getSimpleName() + str;
	}

	private static byte[] getPositiveBytes(byte[] bytes) {
		byte[] result = new byte[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			result[i * 2] = (byte) (bytes[i] & 0xF);
			result[i * 2 + 1] = (byte) (bytes[i] >>> 4 & 0xF);
		}
		return result;
	}

	private static byte[] reverseBytes(byte[] bytes) {
		if (bytes.length % 2 != 0)
			return null;
		byte[] result = new byte[bytes.length / 2];
		for (int i = 0; i < result.length; i++) {
			byte b = (byte) ((bytes[i * 2] & 0xF) + (bytes[i * 2 + 1] << 4));
			result[i] = b;
		}
		return result;
	}

	public static Message unpack(String str) {
		for (String type : map.keySet()) {
			if (str.startsWith(type)) {
				String content = str.substring(type.length());
				Message.Builder builder = map.get(type);
				if (builder == null)
					return null;
				builder.clear();
				byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
				try {
					builder.mergeFrom(reverseBytes(bytes));
					return builder.build();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}
		return null;
	}

	public static void collectAllMessage() {
		Set<Class<?>> classes = ReflectionUtils.getClassesExtendsClass(Message.class);
		for (Class<?> clazz : classes) {
			try {
				Method method = clazz.getDeclaredMethod("newBuilder");
				Object value = method.invoke(null);
				map.put(clazz.getSimpleName(), (Message.Builder) value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int getHashCode(String str) {
		int hash = str.length();
		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) ^ (hash >>> 27)) ^ str.charAt(i);
		}
		return hash;
	}

	public static byte[] int2bytes(int a) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (a & 0xFF);
		bytes[1] = (byte) (a >>> 8 & 0xFF);
		bytes[2] = (byte) (a >>> 16 & 0xFF);
		bytes[3] = (byte) (a >>> 32 & 0xFF);
		return bytes;
	}

	public static String get10(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			StringBuffer tmp = new StringBuffer();
			for (int i = 0; i < 8; i++) {
				String str = ((b & 1) == 1) ? "1" : "0";
				tmp.append(str);
				b = (byte) (b >>> 1);
			}
			sb.append(tmp.reverse());
			sb.append(",");
		}
		return sb.toString();
	}

}
