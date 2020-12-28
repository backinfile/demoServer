package com.backinfile.core;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.backinfile.core.function.Action;
import com.backinfile.support.Log;

/**
 * 开启若干个线程来循环执行一个Action
 */
public class DispatchThreads {
	private final Action run;
	private final int num;

	private final ArrayList<Thread> threads = new ArrayList<>();
	private volatile boolean threadAbort = false;
	private AtomicInteger abortedNum = new AtomicInteger(0);

	public DispatchThreads(int num, Action run) {
		this.run = run;
		this.num = num;
	}

	public DispatchThreads(Action run) {
		this(3, run);
	}

	public void start() {
		for (int i = 0; i < num; i++) {
			Thread thread = new Thread(this::runThread);
			thread.setName("DispatchThread-" + i);
			thread.setDaemon(true);
			thread.start();
			threads.add(thread);
		}
	}

	private void runThread() {
		while (!threadAbort) {

			try {
				run.invoke();
			} catch (Exception e) {
				Log.Core.error("线程运行出错：{0}", e);
			}
		}
		abortedNum.incrementAndGet();
	}

	public void abort() {
		threadAbort = true;
	}

	public void abortSync() {
		threadAbort = true;

		while (!isAborted())
			;
	}

	public final boolean isAborted() {
		return abortedNum.get() >= num;
	}
}
