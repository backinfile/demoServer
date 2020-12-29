package com.backinfile.world.human;

import com.backinfile.core.AbstractService;
import com.backinfile.core.StandAloneService;
import com.backinfile.gen.proxy.RankServiceProxy;
import com.backinfile.support.Log;

@StandAloneService
public class HumanGlobalService extends AbstractService {

	public HumanGlobalService(long serviceId) {
		super(serviceId);
	}

	@Override
	public void startup() {
	}

	@Override
	public void pulse() {
	}

	@Override
	public void pulsePerSec() {
		var proxy = RankServiceProxy.newInstance();
		proxy.getTopHumanId();
		proxy.listenResult(r -> {
			Log.Game.info("got result" + r.getResult());
		});
	}
}
