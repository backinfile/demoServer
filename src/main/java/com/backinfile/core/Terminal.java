package com.backinfile.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

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
	public void listenLastOutCall(Consumer<IResult> consumer, Object[] context) {
		if (lastOutCall == null) {
			Log.Core.error("没有上次调用， 不能监听");
			return;
		}
		WaitResult waitResult = new WaitResult(consumer, context);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void invoke(Call call) {
//		lastInCall = call;
//
//		IService service = mPort.getService(call.to.serviceID);
//		Method method = Distr.getMethodByString(call.method);
//		try {
//			method.invoke(service, call.args);
//		} catch (Exception e) {
//			Log.Core.error("error in invoke service method", e);
//		}
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
