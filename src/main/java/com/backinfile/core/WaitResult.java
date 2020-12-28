package com.backinfile.core;

import java.util.function.Consumer;

public class WaitResult {
	public Consumer<IResult> callBackHandler = null;
	public Object[] contexts;

	public WaitResult(Consumer<IResult> callBackHandler, Object[] contexts) {
		this.callBackHandler = callBackHandler;
		this.contexts = contexts;
	}
}
