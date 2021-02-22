package com.backinfile.world.human;

import com.backinfile.core.AbstractService;
import com.backinfile.core.Node;
import com.backinfile.core.Params;
import com.backinfile.core.Port;
import com.backinfile.core.StandAloneService;
import com.backinfile.event.EventKey;
import com.backinfile.event.EventManager;
import com.backinfile.event.Listener;
import com.backinfile.gen.proxy.HumanGlobalServiceProxy;
import com.backinfile.gen.proxy.HumanServiceProxy;
import com.backinfile.gen.proxy.RankServiceProxy;
import com.backinfile.support.Log;
import com.backinfile.support.Timer;
import com.backinfile.utils.Time2;

@StandAloneService
public class HumanGlobalService extends AbstractService {

	private Timer timer;

	public HumanGlobalService(long serviceId) {
		super(serviceId);
	}

	@Override
	public void startup() {
		timer = new Timer(5 * Time2.SECOND);
	}

	@Override
	public void pulse() {      

	}

	@Override
	public void pulsePerSec() {
		Log.Human.info("global pulse");
		if (timer.isPeriod()) {
			var proxy = RankServiceProxy.newInstance();
			proxy.getTopHumanId();
			proxy.listenResult(r -> {
				int id = r.getResult();
				Log.Human.info("add human:{}", id);
				HumanService service = new HumanService(id);
				Port port = Node.getLocalNode().getPort(HumanGlobalServiceProxy.PortId);
				port.addService(service);
			});

			HumanServiceProxy proxy2 = HumanServiceProxy.newInstance(HumanGlobalServiceProxy.PortId, 3);
			proxy2.printSelf();

			EventManager.fire(EventKey.HUMAN_INIT, "hahah");
		}
	}

	@Listener(EventKey.HUMAN_INIT)
	public static void onHumanInit(Params params) {
		String word = params.getValue();
		Log.Game.info("in onhumanInit:{}", word);
	}
}
