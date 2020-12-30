package com.backinfile.gen.proxy;

import java.util.ArrayList;
import java.util.List;

import com.backinfile.event.EventManager;
import com.backinfile.event.EventManager.ListenerObject;
import com.backinfile.world.human.HumanGlobalService;

public class ListenerBinder {

	public static List<ListenerObject> getEventListeners() {
		List<ListenerObject> list = new ArrayList<EventManager.ListenerObject>();
		list.add(new ListenerObject(2000, HumanGlobalService::onHumanInit));
		return list;
	}
}
