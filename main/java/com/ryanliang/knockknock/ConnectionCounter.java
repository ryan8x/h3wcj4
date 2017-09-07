package com.ryanliang.knockknock;

public class ConnectionCounter {
	private static int connectionCounter = 0;

	public static int getConnectionCounter() {
		return connectionCounter;
	}

	public static void increaseConnectionCounter() {
		connectionCounter++;
	}
	
	public static void decreaseConnectionCounter() {
		connectionCounter--;
	}
}
