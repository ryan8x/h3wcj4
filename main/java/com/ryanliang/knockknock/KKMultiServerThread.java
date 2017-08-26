/**
 *
 * @author Ryan L.
 */

package com.ryanliang.knockknock;

import java.net.*;
import java.io.*;

public class KKMultiServerThread extends Thread {
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	public KKMultiServerThread(Socket socket) {
		super("KKMultiServerThread");
		this.socket = socket;
	}

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

			while ((inputLine = in.readLine()) != null) {
				outputLine = kkp.processInput(inputLine);
				out.println(outputLine);
				if (outputLine.equals("Bye"))  //possible bug here???  but maybe fixed now.
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			closeConnection();
		}
	}

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
}
