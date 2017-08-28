package com.ryanliang.knockknock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class BackgroundSocketClient extends SwingWorker<String, String> {
	
	private String userInput = null;
    private JTextArea chatTextArea;
    private JLabel connectionStatusLabel;
    
	private Socket kkSocket = null;
	
	private String kkServerHost;
	private int kkServerPort;
	
	private String exceptionErrorMessage = "";
	
	private PrintWriter out = null;
	private BufferedReader in = null;
    
    public BackgroundSocketClient(String kkServerHost, int kkServerPort, JLabel connectionStatusLabel, JTextArea chatTextArea){
    	this.kkServerHost = kkServerHost;
    	this.kkServerPort = kkServerPort;
    	this.connectionStatusLabel = connectionStatusLabel;
    	this.chatTextArea = chatTextArea;
    }
	@Override
	public String doInBackground(){
		connectToServer();
		
		return "none";
	}
	
	@Override
	protected void done(){
		if (exceptionErrorMessage.length() > 1){
			connectionStatusLabel.setText("Connection status: Fail");
			chatTextArea.append(exceptionErrorMessage + "\n");
		}
	}
	
    @Override
    protected void process(List<String> chunks) {

    	chatTextArea.append(chunks.get(0) + "\n");
	}
	
	private void connectToServer() {
		try {
			kkSocket = new Socket(InetAddress.getByName(kkServerHost), kkServerPort);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));

			//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer;

			while ((fromServer = in.readLine()) != null) {
				publish(fromServer);
				if (fromServer.equals("Bye")){
					break;
				}

				//wait for user input
				while (userInput == null)
				{
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (userInput != null) {
					out.println(userInput);
					userInput = null;
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + kkServerHost);
			exceptionErrorMessage = "Don't know about host: " + kkServerHost;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + kkServerHost);
			exceptionErrorMessage = "Couldn't get I/O for the connection to: " + kkServerHost;
		}
		finally{
			stopServer();
		}
	}
	
	public void stopServer() {
		
		try {
			if (kkSocket != null){
				out.close();
				out = null;
				in.close();
				in = null;
				kkSocket.close();
				kkSocket = null;
				userInput = "exit";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void processUserInput(String userInput) {
		this.userInput = userInput;
	}
}