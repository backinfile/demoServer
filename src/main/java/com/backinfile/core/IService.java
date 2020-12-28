package com.backinfile.core;

import com.backinfile.support.Log;
import com.backinfile.utils.Utils2;

public interface IService {

	// 初始化函数，如果有长时间操作，需要阻塞式进行
	void startup();

	void pulse();

	default void pulsePerSec() {

	}

	long getServiceId();

	void setPort(Port port);

	default boolean isinitOver() {
		return true;
	}

	default void syncStartup() {
		startup();
		while (!isinitOver()) {
			Log.Core.warn("{} startup...");
			Utils2.sleep(200);
		}
	}
}
