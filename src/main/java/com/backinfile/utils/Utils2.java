package com.backinfile.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Utils2 {

	public static final String UTF8 = "utf-8";
	public static final Random random = new Random();

	public static String getClassPath() {
		return Utils2.class.getClassLoader().getResource("").getPath();
	}

	public static String getProjectPath() {
		return "";
	}

	public static int getHashCode(String str) {
		int h = 0;
		for (int i = 0; i < str.length(); i++) {
			h = 31 * h + str.charAt(i);
		}
		return h;
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || "".equals(str);
	}

	public static boolean contains(int[] values, int key) {
		for (int value : values) {
			if (value == key) {
				return true;
			}
		}
		return false;
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String readline() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			return br.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}


	public static float rand(float from, float to) {
		return ((float) random.nextDouble()) * (to - from) + from;
	}

	public static double rand() {
		return random.nextDouble();
	}

}
