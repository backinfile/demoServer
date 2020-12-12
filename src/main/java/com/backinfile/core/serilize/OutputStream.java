package com.backinfile.core.serilize;

import com.backinfile.support.Log;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OutputStream {

	private final CodedOutputStream stream;
	private final AutoExpandStream expandStream;

	public OutputStream(int capacity) {
		expandStream = new AutoExpandStream(capacity);
		stream = CodedOutputStream.newInstance(expandStream);
	}

	public OutputStream() {
		this(8);
	}

	public byte[] getBytes() {
		return expandStream.bytes();
	}

	public int size() {
		return expandStream.size();
	}

	public void write(Object obj) {
		try {
			writeObject(obj);
			stream.flush();
		} catch (Exception e) {
			Log.Core.error("写入数据失败", e);
		}
	}

	@SuppressWarnings("rawtypes")
	private void writeObject(Object obj) throws Exception {
		if (obj == null) {
			stream.writeInt32NoTag(SerializeTag.NULL);
			return;
		}

		Class<?> clazz = obj.getClass();

		if (clazz == Byte.class) {
			stream.writeInt32NoTag(SerializeTag.BYTE);
			stream.writeRawByte((byte) obj);
		} else if (clazz == byte[].class) {
			byte[] bytes = (byte[]) obj;
			stream.writeInt32NoTag(SerializeTag.BYTE_ARRAY);
			stream.writeInt32NoTag(bytes.length);
			for (Byte b : bytes) {
				stream.writeRawByte(b);
			}
		} else if (clazz == Boolean.class) {
			stream.writeInt32NoTag(SerializeTag.BOOL);
			stream.writeBoolNoTag((boolean) obj);
		} else if (clazz == boolean[].class) {
			boolean[] booleans = (boolean[]) obj;
			stream.writeInt32NoTag(SerializeTag.BOOL_ARRAY);
			stream.writeInt32NoTag(booleans.length);
			for (boolean b : booleans) {
				stream.writeBoolNoTag(b);
			}
		} else if (clazz == Integer.class) {
			stream.writeInt32NoTag(SerializeTag.INT);
			stream.writeInt32NoTag((int) obj);
		} else if (clazz == int[].class) {
			int[] ints = (int[]) obj;
			stream.writeInt32NoTag(SerializeTag.INT_ARRAY);
			stream.writeInt32NoTag(ints.length);
			for (int i : ints) {
				stream.writeInt32NoTag(i);
			}
		} else if (clazz == Long.class) {
			stream.writeInt32NoTag(SerializeTag.LONG);
			stream.writeInt64NoTag((long) obj);
		} else if (clazz == long[].class) {
			long[] ints = (long[]) obj;
			stream.writeInt32NoTag(SerializeTag.LONG_ARRAY);
			stream.writeInt32NoTag(ints.length);
			for (long i : ints) {
				stream.writeInt64NoTag(i);
			}
		} else if (clazz == Float.class) {
			stream.writeInt32NoTag(SerializeTag.FLOAT);
			stream.writeFloatNoTag((float) obj);
		} else if (clazz == float[].class) {
			float[] floats = (float[]) obj;
			stream.writeInt32NoTag(SerializeTag.FLOAT_ARRAY);
			stream.writeInt32NoTag(floats.length);
			for (float f : floats) {
				stream.writeFloatNoTag(f);
			}
		} else if (clazz == Double.class) {
			stream.writeInt32NoTag(SerializeTag.DOUBLE);
			stream.writeDoubleNoTag((double) obj);
		} else if (clazz == double[].class) {
			double[] doubles = (double[]) obj;
			stream.writeInt32NoTag(SerializeTag.DOUBLE_ARRAY);
			stream.writeInt32NoTag(doubles.length);
			for (double d : doubles) {
				stream.writeDoubleNoTag(d);
			}
		} else if (clazz == String.class) {
			stream.writeInt32NoTag(SerializeTag.STRING);
			stream.writeStringNoTag((String) obj);
		} else if (clazz == String[].class) {
			String[] strings = (String[]) obj;
			stream.writeInt32NoTag(SerializeTag.STRING_ARRAY);
			stream.writeInt32NoTag(strings.length);
			for (String str : strings) {
				stream.writeStringNoTag(str);
			}
		} else if (obj instanceof Enum) {
			stream.writeInt32NoTag(SerializeTag.ENUM);
			Enum<?> anEnum = (Enum<?>) obj;
			stream.writeInt32NoTag(SerializableManager.getCommonSerializeID(anEnum));
			stream.writeInt32NoTag(anEnum.ordinal());
		} else if (obj instanceof List) {
			List<?> list = (List<?>) obj;

			stream.writeInt32NoTag(SerializeTag.LIST);
			stream.writeInt32NoTag(list.size());

			for (Object ele : list) {
				write(ele);
			}
		} else if (obj instanceof Set) {
			Set<?> set = (Set<?>) obj;

			stream.writeInt32NoTag(SerializeTag.SET);
			stream.writeInt32NoTag(set.size());

			for (Object ele : set) {
				write(ele);
			}
		} else if (obj instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) obj;

			stream.writeInt32NoTag(SerializeTag.MAP);
			stream.writeInt32NoTag(map.size());

			for (Map.Entry ele : map.entrySet()) {
				write(ele.getKey());
				write(ele.getValue());
			}
		} else if (obj instanceof ISerializable) {
			stream.writeInt32NoTag(SerializeTag.SERIALIZE);
			stream.writeInt32NoTag(SerializableManager.getCommonSerializeID(obj));
			((ISerializable) obj).writeTo(this);
		} else if (obj instanceof Object[]) {
			Object[] array = (Object[]) obj;

			stream.writeInt32NoTag(SerializeTag.ARRAY);
			stream.writeInt32NoTag(array.length);

			for (Object ele : array) {
				write(ele);
			}
		} else if (obj instanceof Message) {
			Message message = (Message) obj;
			byte[] bytes = message.toByteArray();
			int length = bytes.length;
			int id = SerializableManager.getCommonSerializeID(message);

			stream.writeInt32NoTag(SerializeTag.MESSAGE);
			stream.writeInt32NoTag(id);
			stream.writeInt32NoTag(length);
			stream.writeRawBytes(bytes);
		} else {
			throw new Exception("无法序列化" + obj.getClass().getName());
		}
	}
}
