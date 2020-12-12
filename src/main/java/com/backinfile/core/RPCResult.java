package com.backinfile.core;

public class RPCResult implements IRPCResult {

	public static final int ERROR_OK = 0;
	public static final int ERROR_TIMEOUT = 1;
	public static final int ERROR_CORE = 2;

	private int code = ERROR_OK;
	private Params params = null;

	@Override
	public boolean isOK() {
		return code == ERROR_OK;
	}

	@Override
	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}
}
