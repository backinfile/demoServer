package com.backinfile.core;

import java.util.HashMap;
import java.util.Map;

public class ProxyHelper {
	public static CallPoint getCurPortCallPoint() {
		Port curPort = Port.getCurrentPort();
		if (curPort == null) {
			throw new SysException("not excute in port thread!!");
		}
		return new CallPoint("", curPort.getPortId(), 0L);
	}

	private static Map<String, Map<Long, Object>> proxyMethods = new HashMap<>();

}
