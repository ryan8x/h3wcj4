package com.ryanliang.knockknock;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

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
	
	private String userInput = "";
    private JTextArea chatTextArea;
    private JLabel connectionStatusLabel;
    
	private Socket kkSocket = null;
	
	private String kkServerHost;
	private int kkServerPort;
	
	private String exceptionErrorMessage = "";
	
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	private KKProtocolState state = KKProtocolState.WAITING;
    
	/**
	 * This is the only constructor defined for this class.
	 * @param kkServerHost Specifies server host name or ip address
	 * @param kkServerPort Specifies server port number
	 * @param connectionStatusLabel A reference JLabel for updating connection status
	 * @param chatTextArea A reference JTextArea for updating chat text
	 */
    public BackgroundSocketClient(String kkServerHost, int kkServerPort, JLabel connectionStatusLabel, JTextArea chatTextArea){
    	this.kkServerHost = kkServerHost;
    	this.kkServerPort = kkServerPort;
    	this.connectionStatusLabel = connectionStatusLabel;
    	this.chatTextArea = chatTextArea;
    }
    
	/**
	 * This method performs a task in the background in a SwingWorker thread.
	 * @return null 
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
			connectionStatusLabel.setText("<html>Connection status: <font color='red'>Fail</font></html>");
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
			out = new ObjectOutputStream(kkSocket.getOutputStream());
			in = new ObjectInputStream(kkSocket.getInputStream());
			
			KKServerResponse fromServer;

			while ((fromServer = (KKServerResponse) in.readObject()) != null) {

				publish(fromServer.getKnockKnock());
				state = KKProtocolState.SENTKNOCKKNOCK;

				while (!userInput.equalsIgnoreCase("bye")){
				
					if (state == KKProtocolState.SENTKNOCKKNOCK) {
						if (userInput.equalsIgnoreCase(fromServer.getWhoIsThere())){
							publish(fromServer.getJoke().getClue());
							state = KKProtocolState.SENTCLUE;
						}
						else{
							publish(fromServer.getSuppose() + fromServer.getWhoIsThere() + fromServer.getTryAgain());
						}
					}
					else if (state == KKProtocolState.SENTCLUE) {
						if (userInput.equalsIgnoreCase(fromServer.getJoke().getClue() + fromServer.getWho())){
							publish(fromServer.getJoke().getAnswer() + fromServer.getWantMore());	
							state = KKProtocolState.ANOTHER;
						}
						else{
							publish(fromServer.getSuppose() + fromServer.getJoke().getClue() + fromServer.getWho() + fromServer.getTryAgain());
							state = KKProtocolState.SENTKNOCKKNOCK;
						}
					}
					else if (state == KKProtocolState.ANOTHER) {
						if (userInput.equalsIgnoreCase("y")){
							out.writeObject("y");
							out.flush();
						}
						else{
							out.writeObject("n");
							out.flush();
						}
						state = KKProtocolState.WAITING;
						break;
					}
					userInput = "";
					waitForInput();
				}
			}
		} catch (ClassNotFoundException e) {
			System.err.println("Client side socket network communication error is encountered for some reason.");
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
	
	private void waitForInput() {
		//wait for user input
		while (userInput.equals(""))
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				System.err.println("thread sleep method of client SwingWorker is being interrupted.");
			}
		}
	}

	/**
	 * This method is for freeing up resources. 
	 */
	public void stopServer() {
		userInput = "bye";
		try {
			if (kkSocket != null){
				if (out != null){
					out.close();
					out = null;
				}
				if (in != null){
					in.close();
					in = null;
				}
				kkSocket.close();
				kkSocket = null;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("Client socket is being closed.");
		}

	}
	
	/**
	 * This method is for accepting user text input.
	 */
	public void processUserInput(String userInput) {
		this.userInput = userInput;
	}
}