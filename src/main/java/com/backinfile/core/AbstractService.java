package com.backinfile.core;

public abstract class AbstractService implements IService {

	private Port m_port;
	private final long serviceId;

	public AbstractService(long serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public long getServiceId() {
		return serviceId;
	}

	public void returns(Object... results) {
		m_port.returns(results);
	}

	public void returns(Call call, Object... results) {
		m_port.returns(call, results);
	}

	@Override
	public void setPort(Port port) {
		m_port = port;
	}
}
