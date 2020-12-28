package com.backinfile.core.gen;

public class GenManager {
	public static void genAll() {
		GenProxys genProxys = new GenProxys();
		genProxys.genFile();
	}

	public static void main(String[] args) {
		genAll();
	}
}
