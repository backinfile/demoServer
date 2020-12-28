package com.backinfile.world.rank;

import com.backinfile.core.AbstractService;
import com.backinfile.core.ProxyMethod;
import com.backinfile.core.StandAloneService;
import com.backinfile.support.Log;

@StandAloneService
public class RankService extends AbstractService {
	public RankService(long serviceId) {
		super(serviceId);
	}

	@ProxyMethod
	public void getTopHumanId() {
		returns(123L);
	}

	@Override
	public void startup() {
	}

	@Override
	public void pulse() {
	}

	@Override
	public void pulsePerSec() {
		Log.Rank.info("pulse per sec");
	}
}
