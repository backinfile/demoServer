package com.backinfile.core.serilize;

import com.backinfile.support.Log;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Message;

import java.lang.reflect.Constructor;
import java.util.*;

public class InputStream {

	private final CodedInputStream stream;

	public InputStream(byte[] bytes, int start, int length) {
		stream = CodedInputStream.newInstance(bytes, start, length);
	}

	public InputStream(byte[] bytes) {
		this(bytes, 0, bytes.length);
	}

	@SuppressWarnings("unchecked")
	public <T> T read() {
		try {
			return (T) readObject();
		} catch (Exception e) {
			Log.Core.error("读取数据失败", e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object readObject() throws Exception {

		int tag = stream.readInt32();
		if (tag == SerializeTag.NULL)
			return null;

		switch (tag) {
		case SerializeTag.BYTE:
			return stream.readRawByte();
		case SerializeTag.BYTE_ARRAY: {
			int size = stream.readInt32();
			byte[] array = new byte[size];
			for (int i = 0; i < size; i++) {
				array[i] = stream.readRawByte();
			}
			return array;
		}
		case SerializeTag.ARRAY: {
			int size = stream.readInt32();
			Object[] array = new Object[size];
			for (int i = 0; i < size; i++) {
				array[i] = read();
			}
			return array;
		}
		case SerializeTag.BOOL:
			return stream.readBool();
		case SerializeTag.BOOL_ARRAY: {
			int size = stream.readInt32();
			boolean[] array = new boolean[size];
			for (int i = 0; i < size; i++) {
				array[i] = stream.readBool();
			}
			return array;
		}
		case SerializeTag.INT:
			return stream.readInt32();
		case SerializeTag.INT_ARRAY: {
			int size = stream.readInt32();
			int[] array = new int[size];
			for (int i = 0; i < size; i++) {
				array[i] = stream.readInt32();
			}
			return array;
		}
		case SerializeTag.LONG:
			return stream.readInt64();
		case SerializeTag.LONG_ARRAY: {
			int size = stream.readInt32();
			long[] array = new long[size];
			for (int i = 0; i < size; i++) {
				array[i] = stream.readInt64();
			}
			return array;
		}
		case SerializeTag.FLOAT:
			return stream.readFloat();
		case SerializeTag.FLOAT_ARRAY: {
			int size = stream.readInt32();
			float[] array = new float[size];
			for (int i = 0; i < size; i++) {
				array[i] = stream.readFloat();
			}
			return array;
		}
		case SerializeTag.DOUBLE:
			return stream.readDouble();
		case SerializeTag.DOUBLE_ARRAY: {
			int size = stream.readInt32();
			double[] array = new double[size];
			for (int i = 0; i < size; i++) {
				array[i] = stream.readDouble();
			}
			return array;
		}
		case SerializeTag.STRING:
			return stream.readString();
		case SerializeTag.STRING_ARRAY: {
			int size = stream.readInt32();
			String[] array = new String[size];
			for (int i = 0; i < size; i++) {
				array[i] = stream.readString();
			}
			return array;
		}
		case SerializeTag.ENUM: {
			int id = stream.readInt32();
			int ord = stream.readInt32();
			Object[] enumValues = SerializableManager.parseFromSerializeID(id);
			if (enumValues != null) {
				return enumValues[ord];
			} else {
				Log.Core.error("未能序列化{0}", id);
				return null;
			}
		}
		case SerializeTag.LIST: {
			int size = stream.readInt32();
			List<?> list = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				list.add(read());
			}
			return list;
		}
		case SerializeTag.SET: {
			int size = stream.readInt32();
			Set<?> set = new HashSet<>();
			for (int i = 0; i < size; i++) {
				set.add(read());
			}
			return set;
		}
		case SerializeTag.MAP: {
			int size = stream.readInt32();
			Map map = new HashMap<>();

			for (int i = 0; i < size; i++) {
				Object key = read();
				Object value = read();
				map.put(key, value);
			}
			return map;
		}
		case SerializeTag.SERIALIZE: {
			int id = stream.readInt32();
			Constructor<?> constructor = SerializableManager.parseFromSerializeID(id);
			if (constructor != null) {
				ISerializable serializable = (ISerializable) constructor.newInstance();
				serializable.readFrom(this);
				return serializable;
			} else {
				Log.Core.error("未能序列化{0}", id);
				return null;
			}
		}
		case SerializeTag.MESSAGE: {
			int id = stream.readInt32();
			int length = stream.readInt32();
			byte[] bytes = stream.readRawBytes(length);

			Message.Builder builder = SerializableManager.parseFromSerializeID(id);
			if (builder != null) {
				builder.clear();
				return builder.mergeFrom(bytes).build();
			} else {
				Log.Core.error("未能序列化{0}", id);
				return null;
			}
		}
		}

		return null;
	}

}
