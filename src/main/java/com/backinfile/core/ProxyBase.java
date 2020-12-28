package com.backinfile.core;

import java.util.function.Consumer;


public abstract class ProxyBase {
	protected Port m_port;

	public abstract Object getMethod(int methodKey);

	public void listenResult(Consumer<IResult> func, Object... contexts) {
		m_port.listenLastOutCall(func, contexts);
	}
}
