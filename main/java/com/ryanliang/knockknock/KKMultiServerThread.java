package com.ryanliang.knockknock;

import java.net.*;
import java.io.*;

/**
 * KKMultiServerThread is a Thread subclass utilized by the knock knock server app for processing multiple knock knock client simultaneously.
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKMultiServerThread extends Thread {
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private boolean socketAlive = true;

	/**
	 * This is the only constructor defined for this class.
	 * @param socket Is a socket connection with a client
	 */
	public KKMultiServerThread(Socket socket) {
		super("KKMultiServerThread");
		this.socket = socket;
	}

	/**
	 * This method runs on a unique thread and is for processing network communication with the knock knock client.
	 */
	public void run() {

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));

			String inputLine, outputLine;
			KnockKnockProtocol kkp = new KnockKnockProtocol();
			outputLine = kkp.processInput(null);
			out.println(outputLine);

			//Readline() returns null when client side socket is closed.	
			while ((inputLine = in.readLine()) != null) {
				outputLine = kkp.processInput(inputLine);
				out.println(outputLine);
				if (outputLine.equals("Bye"))  
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			closeConnection();
			socketAlive = false;
		}
	}

	/**
	 * This method is for freeing up resources. 
	 */
	public void closeConnection() {

		try {
			if (socket != null){
				out.close();
				out = null;
				in.close();
				in = null;
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	/**
	 * This method returns the status of the socket.
	 * @return A a boolean value.   
	 */
	public boolean isSocketAlive() {
		return socketAlive;
	}
}
