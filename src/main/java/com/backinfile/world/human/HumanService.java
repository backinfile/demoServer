package com.backinfile.world.human;

import com.backinfile.core.AbstractService;
import com.backinfile.core.ProxyMethod;
import com.backinfile.support.Log;

public class HumanService extends AbstractService {

	private HumanObject humanObject;

	public HumanService(long serviceId) {
		super(serviceId);

		humanObject = new HumanObject(serviceId);
	}

	@Override
	public void startup() {
	}

	@Override
	public void pulsePerSec() {
//		Log.Human.info("human pulse: " + getServiceId());
	}

	@Override
	public void pulse() {
	}
	
	@ProxyMethod
	public void printSelf() {
		Log.Human.info("printSelf:{}", getServiceId());
	}
}
