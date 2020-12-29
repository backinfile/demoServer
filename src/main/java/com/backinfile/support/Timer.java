package com.backinfile.support;

import com.backinfile.core.Port;
import com.backinfile.utils.Time2;

public class Timer {
	private long period;
	private long lastTime;

	public Timer(long period) {
		this.period = period;
		this.lastTime = Time2.getCurrentTimestamp();
	}

	public boolean isPeriod() {
		long time = Time2.getCurrentTimestamp();
		var dtime = time - lastTime;
		if (dtime > period) {
			lastTime += period;
			return true;
		} else {
			return false;
		}
	}
}
