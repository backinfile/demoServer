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

import com.backinfile.world.stage.MainStageService;

public class MainStageServiceProxy extends ProxyBase {
	
	public static final String PortId = "com.backinfile.world.stage.MainStageService";
	
	private CallPoint targetCallPoint;

	private MainStageServiceProxy(CallPoint targetCallPoint) {
		this.targetCallPoint = targetCallPoint;
		this.m_port = Port.getCurrentPort();
	}

	public static MainStageServiceProxy newInstance() {
		return new MainStageServiceProxy(new CallPoint(Distr.getDefaultNodeId(), PortId, 0L));
	}

    @SuppressWarnings({"rawtypes"}) 
	public Object getMethod(Object service, int methodKey) {
		MainStageService serv = (MainStageService)service;
		switch (methodKey) {
		default:
			break;
		}
		return null;
	}
}
