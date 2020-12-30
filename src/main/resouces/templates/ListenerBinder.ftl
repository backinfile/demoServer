package com.backinfile.gen.proxy;

import java.util.ArrayList;
import java.util.List;

import com.backinfile.event.EventManager;
import com.backinfile.event.EventManager.ListenerObject;
<#list imports as import>
import ${import};
</#list>

public class ListenerBinder {

	public static List<ListenerObject> getEventListeners() {
		List<ListenerObject> list = new ArrayList<EventManager.ListenerObject>();
<#list functions as func>
		list.add(new ListenerObject(${func.eventKey}, ${func.func}));
</#list>
		return list;
	}
}
