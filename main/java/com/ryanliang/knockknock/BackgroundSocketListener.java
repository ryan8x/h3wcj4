package com.ryanliang.knockknock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
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
public class BackgroundSocketListener extends SwingWorker<Void, String> {
	
    private ServerSocket serverSocket = null;
	private int kkServerPort;
	private String exceptionErrorMessage = "";
	
    private boolean listening = false;
    private List<KKMultiServerThread> socketThreadList = new LinkedList<KKMultiServerThread>();
	private JLabel totalClientConectionLabel;
	private JLabel serverStatusLabel;
	private int totalClientConectionCounter = 0;
	
	/**
	 * This is the only constructor defined for this class.
	 * @param kkServerPort Specifies server port number
	 * @param totalClientConectionLabel2 
	 * @param connectionStatusLabel A reference JLabel for updating total connection status
	 */
    public BackgroundSocketListener(int kkServerPort, JLabel serverStatusLabel, JLabel totalClientConectionLabel){
    	this.kkServerPort = kkServerPort;
    	this.serverStatusLabel = serverStatusLabel;
    	this.totalClientConectionLabel = totalClientConectionLabel;
    }
    
	/**
	 * This method performs a task in the background in a SwingWorker thread.
	 * @return  
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
			serverStatusLabel.setText("Server status: " + exceptionErrorMessage);
		}
	}

	/**
	 * This method updates specific Swing components (UI) while doInBackground() is in progress. 
	 */
    @Override
    protected void process(List<String> chunks) {

    	totalClientConectionLabel.setText("Client connections: " + chunks.get(0));
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
					}
				}
				socketThreadList.clear();
				totalClientConectionCounter = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
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
				
				totalClientConectionCounter++;
				publish(String.valueOf(totalClientConectionCounter));
			}
		}
        catch (IOException e) {
            e.printStackTrace();
		}
	}
}
