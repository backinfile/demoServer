package com.backinfile.core;

public interface IRPCResult {
	boolean isOK();

	Params getParams();

	default <T> T get(String argName) {
		T t = getParams().getValue(argName);
		return t;
	}

	default <T> T get() {
		T t = getParams().getValue();
		return t;
	}
}
