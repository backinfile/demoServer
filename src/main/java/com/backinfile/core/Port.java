package com.backinfile.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.backinfile.core.function.Action;
import com.backinfile.support.Log;
import com.backinfile.utils.Time2;

public class Port implements Delayed, ITerminal {

	private Node node;
	private IService curService = null;

	private String portId;

	public ITerminal terminal;

	/**
	 * 用于检查是否需要心跳操作，（在额外的唤醒中不执行心跳）
	 */
	public volatile boolean pulsed = false;

	// 上次执行时间
	protected long time = 0;

	// 两次执行时间的时间差距
	private long deltaTime = 0;
	private long lastPulsePerSecondTime = 0L;

	// 执行频率（每秒执行几次)
	private int HZ = 33;

	private static final ThreadLocal<Port> curPort = new ThreadLocal<>();

	public static Port getCurrentPort() {
		return curPort.get();
	}

	protected final Map<Object, IService> services = new HashMap<>();
	private final ConcurrentLinkedQueue<IService> servicesWaitForAdd = new ConcurrentLinkedQueue<>();
	private final ConcurrentLinkedQueue<IService> servicesWaitForRemove = new ConcurrentLinkedQueue<>();
	private final List<Action> postActionList = new ArrayList<>();

	public Port(String portId) {
		this.portId = portId;
	}

	public void setNode(Node node) {
		this.node = node;
		terminal = new Terminal(this, node);
	}

	public void caseRunOnce() {

		// 处理rpc
		executeInCall();

		for (IService service : services.values()) {
			curService = service;
			service.pulse();
		}

		if (time - lastPulsePerSecondTime >= Time2.SECOND) {
			lastPulsePerSecondTime = time;
			for (IService service : services.values()) {
				curService = service;
				service.pulsePerSec();
			}
		}

		curService = null;
	}

	public void caseRunOnceBefore() {
		long newTime = node.getTime();
		deltaTime = time > 0 ? newTime - time : 0;
		this.time = newTime;

		curPort.set(this);

		checkCallReturnTimeout();
	}

	public void caseRunOnceAfter() {
		curPort.set(null);

		// 执行post函数
		for (Action action : postActionList) {
			try {
				action.invoke();
			} catch (Exception e) {
				Log.Core.error("error in post action", e);
			}
		}

		// 在心跳结束时添加新的serviced
		while (true) {
			IService service = servicesWaitForAdd.poll();
			if (service == null)
				break;
			service.setPort(this);
			services.put(service.getServiceId(), service);
		}
		while (true) {
			IService service = servicesWaitForRemove.poll();
			if (service == null)
				break;
			services.remove(service);
		}
	}

	/**
	 * 临时唤醒来执行rpc调用
	 */
	public void caseAwakeUp() {
		curPort.set(this);
		executeInCall();
		curPort.set(null);
	}

	/**
	 * 设置每秒执行次数
	 */
	protected void setHZ(int HZ) {
		this.HZ = HZ;
	}

	public IService getCurService() {
		return curService;
	}

	public Node getNode() {
		return node;
	}

	public void addService(IService service) {
		servicesWaitForAdd.add(service);
	}

	public void removeService(IService service) {
		servicesWaitForRemove.add(service);
	}

	public long getTime() {
		if (time > 0)
			return time;
		return System.currentTimeMillis();
	}

	public long getDeltaTime() {
		return deltaTime;
	}

	public IService getService(Object serviceId) {
		return services.get(serviceId);
	}

	/**
	 * 将一个函数推迟到心跳结束执行
	 */
	protected void post(Action action) {
		postActionList.add(action);
	}

	/**
	 * 距离下次执行的时间
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return time + (1000 / HZ) - node.getTime();
	}

	@Override
	public int compareTo(Delayed o) {
		Port port = (Port) o;
		return Long.compare(time + (1000 / HZ), port.time + (1000 / port.HZ));
	}

	@Override
	public void addCall(Call call) {
		terminal.addCall(call);
	}

	@Override
	public Call getLastInCall() {
		return terminal.getLastInCall();
	}

	@Override
	public void sendNewCall(CallPoint to, String method, Object[] args) {
		terminal.sendNewCall(to, method, args);
	}

	@Override
	public void returns(Object... values) {
		terminal.returns(values);
	}

	@Override
	public void returnsError(int errorCode, String error) {
		terminal.returnsError(errorCode, error);
	}

	@Override
	public void returns(Call call, Object... results) {
		terminal.returns(call, results);
	}

	@Override
	public void returnsError(Call call, int errorCode, String error) {
		terminal.returnsError(call, errorCode, error);
	}

	@Override
	public void checkCallReturnTimeout() {
		terminal.checkCallReturnTimeout();
	}

	@Override
	public void listenLastOutCall(Consumer<IResult> consumer, Object... context) {
		terminal.listenLastOutCall(consumer, context);
	}

	@Override
	public void executeInCall() {
		terminal.executeInCall();
	}

	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}
}
