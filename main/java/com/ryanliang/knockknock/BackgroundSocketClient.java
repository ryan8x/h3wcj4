package com.ryanliang.knockknock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class BackgroundSocketClient extends SwingWorker<String, String> {
	
	private String userInput = null;
    private JLabel responseLabelText;
	private Socket kkSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
    
    public BackgroundSocketClient(Socket kkSocket, JLabel label){
    	this.kkSocket = kkSocket;
    	this.responseLabelText = label; 
    }
	@Override
	public String doInBackground(){
		connectToServer();
		
		return "test";
	}
	
    @Override
    protected void process(List<String> chunks) {

			responseLabelText.setText(chunks.get(0));
	}
	
	private void connectToServer() {
		try {
			//kkSocket = new Socket("localhost", 4444);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: localhost");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: localhost");
		}
		
		//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer;
		try {
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
			stopServer();		
		} catch (IOException e) {
			e.printStackTrace();
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