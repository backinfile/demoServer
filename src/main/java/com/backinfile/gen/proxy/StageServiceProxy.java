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

import com.backinfile.world.stage.StageService;

public class StageServiceProxy extends ProxyBase {
	
	
	private CallPoint targetCallPoint;

	private StageServiceProxy(CallPoint targetCallPoint) {
		this.targetCallPoint = targetCallPoint;
		this.m_port = Port.getCurrentPort();
	}

	public static StageServiceProxy newInstance(String portId, long serviceId) {
		return new StageServiceProxy(new CallPoint(Distr.getDefaultNodeId(), portId, serviceId));
	}
	
	public static StageServiceProxy newInstance(CallPoint callPoint) {
		return new StageServiceProxy(callPoint);
	}

    @SuppressWarnings({"rawtypes"}) 
	public Object getMethod(Object service, int methodKey) {
		StageService serv = (StageService)service;
		switch (methodKey) {
		default:
			break;
		}
		return null;
	}
}
