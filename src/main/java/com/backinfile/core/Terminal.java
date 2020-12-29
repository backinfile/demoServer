package com.backinfile.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import com.backinfile.core.function.Action;
import com.backinfile.core.function.Action0;
import com.backinfile.core.function.Action1;
import com.backinfile.core.function.Action2;
import com.backinfile.core.function.Action3;
import com.backinfile.core.function.Action4;
import com.backinfile.core.function.Action5;
import com.backinfile.core.function.Action6;
import com.backinfile.core.function.Action7;
import com.backinfile.support.Log;
import com.backinfile.utils.Time2;

/**
 * rpc终端--port
 */
public class Terminal implements ITerminal {
	private final ConcurrentLinkedQueue<Call> callCacheList = new ConcurrentLinkedQueue<>();
	private final HashMap<Long, WaitResult> waitingResponseList = new HashMap<>();
	private final Queue<Call> calls = new LinkedList<>();
	private Call lastInCall;
	private Call lastOutCall;
	private long call_id_max = 0;
	private final Node mNode;
	private final Port mPort;

	public static final long CALL_EXPIRE_TIME = 30 * Time2.SECOND;

	public Terminal(Port port, Node node) {
		this.mPort = port;
		this.mNode = node;
	}

	@Override
	public void addCall(Call call) {
		callCacheList.add(call);
	}

	@Override
	public Call getLastInCall() {
		return lastInCall;
	}

	@Override
	public void sendNewCall(CallPoint to, int method, Object[] args) {
		Call call = new Call();
		call.from = Node.getCurCallPoint();
		call.to = to;
		call.id = call_id_max++;
		call.expireTime = mNode.getTime() + CALL_EXPIRE_TIME;
		call.method = method;
		call.args = args;

		lastOutCall = call;
		mNode.handleSendCall(call);
	}

	@Override
	public void returns(Object[] values) {
		returns(lastInCall, values);
	}

	@Override
	public void returnsError(int errorCode, String error) {
		returnsError(lastInCall, errorCode, error);
	}

	@Override
	public void returns(Call call, Object... results) {
		Call callReturn = call.newCallReturn(results);
		mNode.handleSendCall(callReturn);
	}

	@Override
	public void returnsError(Call call, int errorCode, String error) {
		Call callReturn = call.newErrorReturn(errorCode, error);
		mNode.handleSendCall(callReturn);
	}

	@Override
	public void checkCallReturnTimeout() {
		flush();
		while (true) {
			Call call = calls.peek();
			if (call == null)
				break;
			if (call.isExpired()) {
				calls.poll();
				returnsError(call, ErrorCode.RPC_CALL_TIMEOUT, "rpc调用超时，已忽略");
			} else {
				break;
			}
		}
	}

	private void flush() {
		while (true) {
			Call call = callCacheList.poll();
			if (call == null)
				break;
			calls.add(call);
		}
	}

	@Override
	public void listenLastOutCall(Consumer<IResult> consumer, Object... contexts) {
		if (lastOutCall == null) {
			Log.Core.error("没有上次调用， 不能监听");
			return;
		}
		WaitResult waitResult = new WaitResult(consumer, contexts);
		waitingResponseList.put(lastOutCall.id, waitResult);
	}

	@Override
	public void executeInCall() {
		flush();
		while (true) {

			Call call = calls.poll();
			if (call == null)
				break;

			try {
				if (call.type == Call.RPC_TYPE_CALL) {
					invoke(call);
				} else if (call.type == Call.RPC_TYPE_CALL_RETURN) {
					processCallReturn(call);
				} else if (call.type == Call.RPC_TYPE_CALL_RETURN_ERROR) {
					processCallReturn(call);
				}
			} catch (Exception e) {
				Log.Core.error("error in execute inCall", e);
			}
		}
	}

	private void processCallReturn(Call call) {
		if (waitingResponseList.containsKey(call.id)) {
			WaitResult waitResult = waitingResponseList.remove(call.id);
			Result result = new Result(call.args);
			result.setError(call.type == Call.RPC_TYPE_CALL_RETURN_ERROR);
			result.updateContexts(waitResult.contexts);
			waitResult.callBackHandler.accept(result);
		}
	}

	private final Map<String, ProxyBase> cachedProxy = new HashMap<>();
	private static final String PackagePath = "com.backinfile.gen.proxy.";

	private void invoke(Call call) {
		lastInCall = call;
		IService service = mPort.getService(call.to.serviceID);
		ProxyBase proxy = cachedProxy.get(call.to.portID);
		if (proxy == null) {
			try {
				Class<?> clazz = Class.forName(PackagePath + service.getClass().getSimpleName() + "Proxy");
				var c = clazz.getDeclaredConstructor(CallPoint.class);
				c.setAccessible(true);
				proxy = (ProxyBase) c.newInstance((Object) null);
				cachedProxy.put(call.to.portID, proxy);
			} catch (Exception e) {
				Log.Core.error("find proxy class failed", e);
			}
		}

		if (proxy != null) {
			Object method = proxy.getMethod(service, call.method);
			try {
				invoke(method, call.args);
			} catch (Exception e) {
				Log.Core.error("invoke service's method error", e);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void invoke(Object method, Object[] args) {
		switch (args.length) {
		case 0:
			((Action0) method).invoke();
			break;
		case 1:
			((Action1) method).invoke(args[0]);
			break;
		case 2:
			((Action2) method).invoke(args[0], args[1]);
			break;
		case 3:
			((Action3) method).invoke(args[0], args[1], args[2]);
			break;
		case 4:
			((Action4) method).invoke(args[0], args[1], args[2], args[3]);
			break;
		case 5:
			((Action5) method).invoke(args[0], args[1], args[2], args[3], args[4]);
			break;
		case 6:
			((Action6) method).invoke(args[0], args[1], args[2], args[3], args[4], args[5]);
			break;
		case 7:
			((Action7) method).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
			break;
		default:
			Log.Core.error("rpc函数参数超长！");
			break;
		}
	}

	/*
	 * Object method = service.getMethodFunction(call.method); switch
	 * (call.args.length) { case 0: ((Action) method).invoke(); case 1: ((Action1)
	 * method).invoke(call.args[0]); case 2: ((Action2) method).invoke(call.args[0],
	 * call.args[1]); case 3: ((Action3) method).invoke(call.args[0], call.args[1],
	 * call.args[2]); case 4: ((Action4) method).invoke(call.args[0], call.args[1],
	 * call.args[2], call.args[3]); case 5: ((Action5) method).invoke(call.args[0],
	 * call.args[1], call.args[2], call.args[3], call.args[4]); default:
	 * Log.Core.error("rpc调用参数过长，忽略此调用"); }
	 */
}
