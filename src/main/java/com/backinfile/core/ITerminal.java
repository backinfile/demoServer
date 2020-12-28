package com.backinfile.core;

import java.util.function.Consumer;

public interface ITerminal {

	// 接受一个来自远程的Call
	void addCall(Call call);

	Call getLastInCall();

	void sendNewCall(CallPoint to, String method, Object[] args);

	void returns(Object... values);

	void returnsError(int errorCode, String error);

	void returns(Call call, Object... results);

	void returnsError(Call call, int errorCode, String error);

	void checkCallReturnTimeout();

	void listenLastOutCall(Consumer<IResult> consumer, Object... context);

	void executeInCall();
}
