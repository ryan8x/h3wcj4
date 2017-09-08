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
	public static int getConnectionCounter() {
		//This method not need to be synchronized as it is read only.
		return connectionCounter;
	}

	/**
	 * This method increases the counter by 1. 
	 */
	public static synchronized void increaseConnectionCounter() {
		//This method may not need to be synchronized as server will only accept client connection one at a time.
		connectionCounter++;
	}
	
	/**
	 * This method decreases the counter by 1. 
	 */
	public static synchronized void decreaseConnectionCounter() {
		//This method needs to be synchronized as multiple client connections can be disconnected simultaneously.
		connectionCounter--;
	}
}
