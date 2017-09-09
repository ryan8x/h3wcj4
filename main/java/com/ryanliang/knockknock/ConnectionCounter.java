package com.ryanliang.knockknock;

/**
 * ConnectionCounter keeps track of current client connections. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class ConnectionCounter {
	private static int connectionCounter = 0;

	/**
	 * This method return the value of the counter.
	 * @return The value of the counter 
	 */
	public static synchronized int getConnectionCounter() {

		return connectionCounter;
	}

	/**
	 * This method increases the counter by 1. 
	 */
	public static synchronized void increaseConnectionCounter() {

		connectionCounter++;
	}
	
	/**
	 * This method decreases the counter by 1. 
	 */
	public static synchronized void decreaseConnectionCounter() {
		//This method needs to be synchronized as multiple client connections can be disconnected simultaneously.
		connectionCounter--;
	}
	
	/**
	 * This method resets the counter to zero. 
	 */
	public static synchronized void resetConnectionCounter() {

		connectionCounter = 0;
	}
}
