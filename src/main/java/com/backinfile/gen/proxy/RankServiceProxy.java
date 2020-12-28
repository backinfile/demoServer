package com.backinfile.gen.proxy;

import com.backinfile.core.CallPoint;
import com.backinfile.core.Distr;
import com.backinfile.core.Port;
import com.backinfile.core.ProxyBase;
import com.backinfile.core.function.Action1;
import com.backinfile.core.function.Action2;
import com.backinfile.core.function.Action3;
import com.backinfile.core.function.Action4;
import com.backinfile.core.function.Action5;
import com.backinfile.core.function.Action6;
import com.backinfile.core.function.Action7;

import com.backinfile.world.rank.RankService;
import java.lang.Integer;
import java.util.ArrayList;

public class RankServiceProxy extends ProxyBase {
	private CallPoint targetCallPoint;

	private RankServiceProxy(CallPoint targetCallPoint) {
		this.targetCallPoint = targetCallPoint;
		this.m_port = Port.getCurrentPort();
	}

	public RankServiceProxy newInstance() {
		return new RankServiceProxy(new CallPoint(Distr.getDefaultNodeId(), "com.backinfile.world.rank.RankService", 0L));
	}

    public void getTopHumanId() {
		m_port.sendNewCall(targetCallPoint, 936437763, new Object[] {});
    }
    
    @SuppressWarnings({"rawtypes"}) 
    public void getTopHumanId(int num, int[] args, ArrayList ints) {
		m_port.sendNewCall(targetCallPoint, 426424240, new Object[] {num, args, ints});
    }
    
    @SuppressWarnings({"rawtypes"}) 
	public Object getMethod(int methodKey) {
		switch (methodKey) {
		case 936437763:
			return (Action1<RankService>) RankService::getTopHumanId;
		case 426424240:
			return (Action4<RankService, Integer, int[], ArrayList>) RankService::getTopHumanId;
		default:
			break;
		}
		return null;
	}
}
