package com.backinfile.core;

import java.util.Scanner;

public class ShutdownThread extends Thread {
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while (!scanner.next().equals("exit")) {
		}
		scanner.close();
		shutdown();
	}

	public void shutdown() {
		Node.getLocalNode().abort();
	}
}
