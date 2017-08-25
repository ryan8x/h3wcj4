/**
 *
 * @author Ryan L.
 */


package com.ryanliang.knockknock;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class KKClient {
	public static void main(String[] args) {
		Socket kkSocket = null;
		EventQueue.invokeLater(() -> {

			connectToServer(kkSocket);
			KKClientGui kkClient = new KKClientGui(kkSocket);

			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenWidth = (int) (screenSize.width*0.2);
			int screenHeight = (int) (screenSize.height*0.2);
			kkClient.setSize(screenWidth, screenHeight);

			kkClient.setLocationRelativeTo(null);
			kkClient.setVisible(true);

		});

	}

	private static void connectToServer(Socket kkSocket) {
		try {
			kkSocket = new Socket("localhost", 4444);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: localhost");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: localhost");
			System.exit(0);
		}
	}

}
