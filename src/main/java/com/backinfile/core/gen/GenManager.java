package com.backinfile.core.gen;

import com.backinfile.support.Log;

public class GenManager {
	public static void genAll() {
		{
			int code = new GenProxyClass().genFile();
			if (code != 0) {
				Log.Gen.warn("生成失败 class={} code={}", GenProxyClass.class.getName(), code);
			}
		}

//		{
//			int code = new GenProxys().genFile();
//			if (code != 0) {
//				Log.Gen.warn("生成失败 class={} code={}", GenProxyClass.class.getName(), code);
//			}
//		}

	}

	public static void main(String[] args) {
		genAll();
	}
}
