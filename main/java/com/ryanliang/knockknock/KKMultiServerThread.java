package com.ryanliang.knockknock;

import java.net.*;
import java.io.*;

/**
 * KKMultiServerThread is a Thread subclass utilized by the knock knock server app for processing multiple knock knock client simultaneously.
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKMultiServerThread implements Runnable {
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	/**
	 * This is the only constructor defined for this class.
	 * @param socket Is a socket connection with a client
	 */
	public KKMultiServerThread(Socket socket) {
		this.socket = socket;
		ConnectionCounter.increaseConnectionCounter();
	}

	/**
	 * This method runs on a unique thread and is for processing network communication with the knock knock client.
	 */
	public void run() {

		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			String inputLine;
			KKJoke outputLine;
			KnockKnockProtocol kkp = new KnockKnockProtocol();
			outputLine = kkp.processInput();
			out.writeObject(new KKServerResponse(outputLine));
			out.flush();
			
			//readObject() returns null when client side socket is closed.	
			while ((inputLine = (String) in.readObject()) != null) {
				if (inputLine.equalsIgnoreCase("y")){
					outputLine = kkp.processInput();
					out.writeObject(new KKServerResponse(outputLine));
					out.flush();
				}
				else if (inputLine.equalsIgnoreCase("bye") || inputLine.equalsIgnoreCase("n")){ 
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			//e.printStackTrace();
			System.err.println("Server side socket network communication error is encountered for some reason.");
		}
		finally{
			closeConnection();
			ConnectionCounter.decreaseConnectionCounter();
		}
	}

	/**
	 * This method is for freeing up resources. 
	 */
	public void closeConnection() {

		try {
			if (socket != null){
				if (out != null){
					out.close();
					out = null;
				}
				if (in != null){
					in.close();
					in = null;
				}
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("Server side socket is being closed.");
		}			
	}
}
