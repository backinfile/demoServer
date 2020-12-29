package com.backinfile.seam;

import com.backinfile.core.Distr;
import com.backinfile.core.Node;
import com.backinfile.core.ShutdownThread;
import com.backinfile.core.serilize.SerializableManager;
import com.backinfile.event.EventManager;
import com.backinfile.support.Log;
import com.backinfile.utils.Utils2;

public class StartUp {
	public static void main(String[] args) {

		Log.Core.info("node 准备中.....");

		SerializableManager.registerAll(); // 序列化
		EventManager.collectAllEventListener();
//    	MessageManager.collectAllMessage(); // proto

		Node node = new Node(Distr.getDefaultNodeId());
		node.localStartUp();
		Log.Core.info("node 已启动.....");

		ShutdownThread hook = new ShutdownThread();
		Runtime.getRuntime().addShutdownHook(hook);
		hook.start();
	}
}
