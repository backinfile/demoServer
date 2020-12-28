package com.backinfile.world.rank;

import com.backinfile.core.CallPoint;
import com.backinfile.core.Distr;
import com.backinfile.core.Port;
import com.backinfile.core.ProxyBase;
import com.backinfile.core.function.Action1;

public class RankServiceProxy extends ProxyBase {
	private Port m_port;
	private CallPoint targetCallPoint;

	private RankServiceProxy(CallPoint targetCallPoint) {
		this.targetCallPoint = targetCallPoint;
		this.m_port = Port.getCurrentPort();
	}

	// 独立service
	public RankServiceProxy newInstance() {
		return new RankServiceProxy(new CallPoint(Distr.getDefaultNodeId(), RankService.class.getName(), 0L));
	}

	// 普通service
	public RankServiceProxy newInstance(long serviceId) {
		return new RankServiceProxy(new CallPoint(Distr.getDefaultNodeId(), RankService.class.getName(), serviceId));
	}

	public void getTopHumanId() {
		m_port.sendNewCall(targetCallPoint, 123, new Object[] {});
	}

	public Object getMethod(int methodKey) {
		switch (methodKey) {
		case 123:
			return (Action1<RankService>) RankService::getTopHumanId;

		default:
			break;
		}
		return null;
	}
}
