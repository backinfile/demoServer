package com.backinfile.world.rank;


import com.backinfile.core.AbstractService;
import com.backinfile.core.ProxyMethod;
import com.backinfile.core.StandAloneService;

@StandAloneService
public class RankService extends AbstractService {
	private int value = 0;

	public RankService(long serviceId) {
		super(serviceId);
	}

	@ProxyMethod
	public void getTopHumanId() {
		returns(value++);
	}

//	@ProxyMethod
//	public void getTopHumanId(int num, int[] args, ArrayList<Integer> ints) {
//
//	}

	@Override
	public void startup() {
	}

	@Override
	public void pulse() {
	}

	@Override
	public void pulsePerSec() {
	}
}
