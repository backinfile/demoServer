package com.backinfile.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.backinfile.core.Params;
import com.backinfile.core.function.Action1;
import com.backinfile.core.function.Function;
import com.backinfile.support.Log;

public class EventManager {
	private static final Map<Integer, List<ListenerObject>> eventMap = new HashMap<>();
	private static final List<Function<List<ListenerObject>>> collectors = new ArrayList<>();

	public static class ListenerObject {
		public int eventKey;
		public Action1<Params> function;

		public ListenerObject(int eventKey, Action1<Params> function) {
			this.eventKey = eventKey;
			this.function = function;
		}
	}

	public static void collectAllEventListener() {
		for (var function : collectors) {
			var listeners = function.invoke();
			for (var listener : listeners) {
				eventMap.computeIfAbsent(listener.eventKey, key -> new ArrayList<>());
				eventMap.get(listener.eventKey).add(listener);
			}
		}
	}

	public static void addCollector(Function<List<ListenerObject>> collector) {
		collectors.add(collector);
	}

	public static void fire(int eventKey, Params params) {
		var list = eventMap.get(eventKey);
		if (list == null) {
			return;
		}
		for (var action : list) {
			try {
				action.function.invoke(params);
			} catch (Exception e) {
				Log.Core.error("run event listener function error", e);
			}
		}
	}
}
