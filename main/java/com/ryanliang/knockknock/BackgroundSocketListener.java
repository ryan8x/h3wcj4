package com.ryanliang.knockknock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 * BackgroundSocketListener is a SwingWorker subclass utilized by the knock knock server app for processing socket network communication with a knock knock client. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class BackgroundSocketListener extends SwingWorker<Void, Void> {
	
    private ServerSocket serverSocket = null;
	private int kkServerPort;
	private String exceptionErrorMessage = "";
	
    private boolean listening = false;
    private List<KKMultiServerThread> socketThreadList = new LinkedList<KKMultiServerThread>();
	private JLabel serverStatusLabel;
	
	/**
	 * This is the only constructor defined for this class.
	 * @param kkServerPort Specifies server port number
	 * @param totalClientConectionLabel2 
	 * @param connectionStatusLabel A reference JLabel for updating total connection status
	 */
    public BackgroundSocketListener(int kkServerPort, JLabel serverStatusLabel){
    	this.kkServerPort = kkServerPort;
    	this.serverStatusLabel = serverStatusLabel;
    }
    
	/**
	 * This method performs a task in the background in a SwingWorker thread.
	 * @return null 
	 */
	@Override
	public Void doInBackground(){
		listenForClients();
		
		return null;
	}
	
	/**
	 * This method updates specific Swing components (UI) when doInBackground() is completed. 
	 */
	@Override
	protected void done(){
		if (exceptionErrorMessage.length() > 1){
			serverStatusLabel.setText("<html>Connection status: <font color='red'>" + exceptionErrorMessage + "</font></html>");
		}
	}
  
	/**
	 * This method is for freeing up resources. 
	 */
	public void stopServer() {

		try {
			if (serverSocket != null){
				serverSocket.close();
				serverSocket = null;
						
				for (KKMultiServerThread socketThread : socketThreadList){
					if (socketThread != null){
						socketThread.closeConnection();
						socketThread = null;
					}
				}
				socketThreadList.clear();
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("Server socket is being closed.");
		}
		finally{
			listening = false;
		}

	}

	/**
	 * This method is for establishing connection with client connection. 
	 */
	private void listenForClients() {
		listening = true;
		
        try {
            serverSocket = new ServerSocket(kkServerPort);
        } catch (IOException e) {
            exceptionErrorMessage = "Could not listen on port " + kkServerPort;
            System.err.println(exceptionErrorMessage);
        }

        try {
			while (listening){
				KKMultiServerThread serverSocketThread = new KKMultiServerThread(serverSocket.accept());
				socketThreadList.add(serverSocketThread);
				serverSocketThread.start();
			}
		}
        catch (IOException e) {
            //e.printStackTrace();
			System.err.println("Server listening socket is encountering an error likely due socket being closed.");
		}
	}
}
