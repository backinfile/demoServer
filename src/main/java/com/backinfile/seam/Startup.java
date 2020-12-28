package com.backinfile.seam;

import com.backinfile.core.Distr;
import com.backinfile.core.Node;
import com.backinfile.support.Log;
import com.backinfile.utils.Utils2;

public class Startup {
	public static void main(String[] args) {

		Log.Core.info("node 准备中.....");

//    	MessageManager.collectAllMessage(); // proto
//        SerializableManager.registerAll();	// 序列化
//        Distr.CollectMethodFromAllStandaloneService(); // rpc

		Node node = new Node(Distr.getDefaultNodeId());
		node.localStartUp();
		Log.Core.info("node 已启动.....");

		while (!Utils2.readline().toLowerCase().equals("exit")) {
//            StageService worldService = new StageService("stageService" + i++);
//            worldService.startUp();
//            node.addPort(worldService);
		}

		node.abort();
	}
}
