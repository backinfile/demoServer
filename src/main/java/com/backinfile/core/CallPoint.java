package com.backinfile.core;

import com.backinfile.core.serilize.ISerializable;
import com.backinfile.core.serilize.InputStream;
import com.backinfile.core.serilize.OutputStream;

import java.util.Objects;

public class CallPoint implements ISerializable {
	public String nodeID; // 不用填
	public String portID;
	public long serviceID;

	/**
	 * 仅供序列化使用
	 */
	public CallPoint() {
	}

	@Override
	public void writeTo(OutputStream out) {
		out.write(nodeID);
		out.write(portID);
		out.write(serviceID);
	}

	@Override
	public void readFrom(InputStream in) {
		nodeID = in.read();
		portID = in.read();
		serviceID = in.read();
	}

	public CallPoint(String nodeID, String portID, long serviceID) {
		this.nodeID = nodeID;
		this.portID = portID;
		this.serviceID = serviceID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof CallPoint))
			return false;
		CallPoint callPoint = (CallPoint) o;
		return Objects.equals(nodeID, callPoint.nodeID) && Objects.equals(portID, callPoint.portID)
				&& Objects.equals(serviceID, callPoint.serviceID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nodeID, portID, serviceID);
	}

}
