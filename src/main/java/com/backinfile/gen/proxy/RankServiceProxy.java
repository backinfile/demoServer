package com.backinfile.gen.proxy;

import com.backinfile.core.CallPoint;
import com.backinfile.core.Distr;
import com.backinfile.core.Port;
import com.backinfile.core.ProxyBase;
import com.backinfile.core.function.Action0;
import com.backinfile.core.function.Action1;
import com.backinfile.core.function.Action2;
import com.backinfile.core.function.Action3;
import com.backinfile.core.function.Action4;
import com.backinfile.core.function.Action5;
import com.backinfile.core.function.Action6;
import com.backinfile.core.function.Action7;

import com.backinfile.world.rank.RankService;

public class RankServiceProxy extends ProxyBase {
	
	public static final String PortId = "com.backinfile.world.rank.RankService";
	
	private CallPoint targetCallPoint;

	private RankServiceProxy(CallPoint targetCallPoint) {
		this.targetCallPoint = targetCallPoint;
		this.m_port = Port.getCurrentPort();
	}

	public static RankServiceProxy newInstance() {
		return new RankServiceProxy(new CallPoint(Distr.getDefaultNodeId(), PortId, 0L));
	}

    public void getTopHumanId() {
		m_port.sendNewCall(targetCallPoint, 936437763, new Object[] {});
    }
    
    @SuppressWarnings({"rawtypes"}) 
	public Object getMethod(Object service, int methodKey) {
		RankService serv = (RankService)service;
		switch (methodKey) {
		case 936437763:
			return (Action0) serv::getTopHumanId;
		default:
			break;
		}
		return null;
	}
}
