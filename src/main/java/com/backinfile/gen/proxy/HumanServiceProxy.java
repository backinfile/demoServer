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

import com.backinfile.world.human.HumanService;

public class HumanServiceProxy extends ProxyBase {
	
	
	private CallPoint targetCallPoint;

	private HumanServiceProxy(CallPoint targetCallPoint) {
		this.targetCallPoint = targetCallPoint;
		this.m_port = Port.getCurrentPort();
	}

	public static HumanServiceProxy newInstance(String portId, long serviceId) {
		return new HumanServiceProxy(new CallPoint(Distr.getDefaultNodeId(), portId, serviceId));
	}
	
	public static HumanServiceProxy newInstance(CallPoint callPoint) {
		return new HumanServiceProxy(callPoint);
	}

    public void printSelf() {
		m_port.sendNewCall(targetCallPoint, -1659102443, new Object[] {});
    }
    
    @SuppressWarnings({"rawtypes"}) 
	public Object getMethod(Object service, int methodKey) {
		HumanService serv = (HumanService)service;
		switch (methodKey) {
		case -1659102443:
			return (Action0) serv::printSelf;
		default:
			break;
		}
		return null;
	}
}
