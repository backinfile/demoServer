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

import com.backinfile.world.human.HumanGlobalService;

public class HumanGlobalServiceProxy extends ProxyBase {
	private CallPoint targetCallPoint;

	private HumanGlobalServiceProxy(CallPoint targetCallPoint) {
		this.targetCallPoint = targetCallPoint;
		this.m_port = Port.getCurrentPort();
	}

	public static HumanGlobalServiceProxy newInstance() {
		return new HumanGlobalServiceProxy(new CallPoint(Distr.getDefaultNodeId(), "com.backinfile.world.human.HumanGlobalService", 0L));
	}

    @SuppressWarnings({"rawtypes"}) 
	public Object getMethod(Object service, int methodKey) {
		HumanGlobalService serv = (HumanGlobalService)service;
		switch (methodKey) {
		default:
			break;
		}
		return null;
	}
}
