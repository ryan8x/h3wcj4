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
public class BackgroundSocketListener extends SwingWorker<String, String> {
	
    private ServerSocket serverSocket = null;
	private int kkServerPort;
    private boolean listening = false;
    private List<KKMultiServerThread> socketThreadList = new LinkedList<KKMultiServerThread>();
	private JLabel totalClientConectionLabel;
	private int totalClientConectionCounter = 0;
	
	/**
	 * This is the only constructor defined for this class.
	 * @param kkServerPort Specifies server port number
	 * @param connectionStatusLabel A reference JLabel for updating total connection status
	 */
    public BackgroundSocketListener(int kkServerPort, JLabel totalClientConectionLabel){
    	this.kkServerPort = kkServerPort;
    	this.totalClientConectionLabel = totalClientConectionLabel;
    }
    
	/**
	 * This method performs a task in the background in a SwingWorker thread.
	 * @return  
	 */
	@Override
	public String doInBackground(){
		listenForClients();
		
		return "none";
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
            System.err.println("Could not listen on port:  " + kkServerPort);
            e.printStackTrace();
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
