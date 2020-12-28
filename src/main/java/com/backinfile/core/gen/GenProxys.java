package com.backinfile.core.gen;


public class GenProxys extends GenBase {
	public GenProxys() {
		super();
		setTemplateFileName("proxy.ftl");
		setTargetPackage("com.backinfile.gen.proxy");
		setFileName("Proxy.txt");
	}

	@Override
	public int genFile() {
		rootMap.put("hello", "hello");
		return super.genFile();
	}
}
