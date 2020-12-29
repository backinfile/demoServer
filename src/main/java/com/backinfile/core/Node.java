package com.backinfile.core;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;

import com.backinfile.support.Log;
import com.backinfile.utils.ReflectionUtils;
import com.backinfile.utils.Utils2;

public class Node {
	private final ConcurrentLinkedQueue<Port> portsWaitForRun = new ConcurrentLinkedQueue<>();
	private final DelayQueue<Port> portsWaitForReschedule = new DelayQueue<>();
	private final ConcurrentHashMap<String, Port> allPorts = new ConcurrentHashMap<>();
	private DispatchThreads dispatchThreads;
	private static final int THREAD_NUM = 3;
	private static final Map<String, Node> nodeMap = new ConcurrentHashMap<>();

	public final String nodeId;

	public Node(String nodeId) {
		this.nodeId = nodeId;
		nodeMap.put(nodeId, this);
	}

	public void startUp() {
		Thread.currentThread().setName("Thread-Node");
		dispatchThreads = new DispatchThreads(THREAD_NUM, this::dispatchRun);
		dispatchThreads.start();
	}

	public void localStartUp() {
		Set<Class<?>> classes = ReflectionUtils.getClassesExtendsClassAndWithAnnotation(IService.class,
				StandAloneService.class);
		for (Class<?> clazz : classes) {
			try {
				IService service = (IService) clazz.getDeclaredConstructor(long.class).newInstance(0L);

				String portId = clazz.getName();
				Port port = new Port(portId);
				port.addService(service);
				this.addPort(port);
				Log.Core.info("{} start", portId);
			} catch (Exception e) {
				Log.Core.error("create service failed: " + clazz.getName(), e);
			}
		}

		startUp();
	}

	public void abort() {
		Log.Core.info("node 中断中.....");
		dispatchThreads.abortSync();
		Log.Core.info("node 中断结束");
	}

	public void addPort(Port port) {
		port.setNode(this);
		portsWaitForRun.add(port);
		allPorts.put(port.getPortId(), port);
	}

	private void dispatchRun() {
		Port port = portsWaitForRun.poll();
		if (port == null) {
			reSchedule(THREAD_NUM);
			Utils2.sleep(1);
			return;
		}
		pulsePort(port);
	}

	private void pulsePort(Port port) {
		if (!port.pulsed) {
			port.caseRunOnceBefore();
			port.caseRunOnce();
			port.caseRunOnceAfter();
			port.pulsed = true;
		} else {
			port.caseAwakeUp();
		}
		portsWaitForReschedule.add(port);
	}

	// 立即唤醒一个port
	private void awake(Port port) {
		if (portsWaitForReschedule.remove(port)) {
			portsWaitForRun.add(port);
		}
	}

	// 将已经被执行过的port重新放入执行队列
	private void reSchedule(int num) {
		for (int i = 0; i < num; i++) {
			Port port = portsWaitForReschedule.poll();
			if (port == null) {
				break;
			}
			port.pulsed = false;
			portsWaitForRun.add(port);
		}
	}

	public long getTime() {
		return System.currentTimeMillis();
	}

	public static CallPoint getCurCallPoint() {
		CallPoint callPoint = new CallPoint();
		callPoint.nodeID = Distr.getDefaultNodeId();
		Port curPort = Port.getCurrentPort();
		callPoint.portID = curPort.getPortId();
		callPoint.serviceID = curPort.getCurService().getServiceId();
		return callPoint;
	}

	public Port getPort(String portId) {
		return allPorts.get(portId);
	}

	public void handleSendCall(Call call) {
		if (!nodeId.equals(call.to.nodeID)) {
			Log.Core.error("此call发送到未知node，已忽略");
			return;
		}

		Port port = getPort(call.to.portID);
		if (port == null) {
			Log.Core.error("此call发送到未知port({})，已忽略", call.to.portID);
			return;
		}

		port.addCall(call);
		awake(port);
	}

	public static Node getNode(String nodeId) {
		return nodeMap.get(nodeId);
	}

	public static Node getLocalNode() {
		return getNode(Distr.getDefaultNodeId());
	}

}
