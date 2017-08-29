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

/**
 * BackgroundSocketClient is a SwingWorker subclass utilized by the knock knock client app for processing socket network communication with the knock knock server. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class BackgroundSocketClient extends SwingWorker<Void, String> {
	
	private String userInput = null;
    private JTextArea chatTextArea;
    private JLabel connectionStatusLabel;
    
	private Socket kkSocket = null;
	
	private String kkServerHost;
	private int kkServerPort;
	
	private String exceptionErrorMessage = "";
	
	private PrintWriter out = null;
	private BufferedReader in = null;
    
	/**
	 * This is the only constructor defined for this class.
	 * @param kkServerHost Specifies server host name or ip address
	 * @param kkServerPort Specifies server port number
	 * @param connectionStatusLabel A reference JLabel for updating connection status
	 * @param chatTextArea A reference JTextArea for updating chat text.
	 */
    public BackgroundSocketClient(String kkServerHost, int kkServerPort, JLabel connectionStatusLabel, JTextArea chatTextArea){
    	this.kkServerHost = kkServerHost;
    	this.kkServerPort = kkServerPort;
    	this.connectionStatusLabel = connectionStatusLabel;
    	this.chatTextArea = chatTextArea;
    }
    
	/**
	 * This method performs a task in the background in a SwingWorker thread.
	 * @return  
	 */
	@Override
	public Void doInBackground(){
		connectToServer();
		
		return null;
	}
	
	/**
	 * This method updates specific Swing components (UI) when doInBackground() is completed. 
	 */
	@Override
	protected void done(){
		if (exceptionErrorMessage.length() > 1){
			connectionStatusLabel.setText("Connection status: Fail");
			chatTextArea.append(exceptionErrorMessage + "\n");
		}
	}
	
	/**
	 * This method updates specific Swing components (UI) while doInBackground() is in progress. 
	 */
    @Override
    protected void process(List<String> chunks) {

    	chatTextArea.append(chunks.get(0) + "\n");
	}
	
	/**
	 * This method is for establishing connection to the server. 
	 */
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
			exceptionErrorMessage = "Don't know about host " + kkServerHost;
			System.err.println(exceptionErrorMessage);
		} catch (IOException e) {
			exceptionErrorMessage = "Couldn't get I/O for the connection to " + kkServerHost + ":" + kkServerPort;
			System.err.println(exceptionErrorMessage);
		}
		finally{
			stopServer();
		}
	}
	
	/**
	 * This method is for freeing up resources. 
	 */
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
	
	/**
	 * This method is for accepting user text input.
	 */
	public void processUserInput(String userInput) {
		this.userInput = userInput;
	}
}