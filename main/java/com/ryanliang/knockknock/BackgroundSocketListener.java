package com.ryanliang.knockknock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class BackgroundSocketListener extends SwingWorker<String, String> {
	
    private ServerSocket serverSocket = null;
    private boolean listening = false;
    private List<KKMultiServerThread> socketThreadList = new LinkedList<KKMultiServerThread>();
	private JLabel totalClientConectionLabel;
	private int totalClientConectionCounter = 0;
	
    public BackgroundSocketListener(JLabel totalClientConectionLabel){
    	this.totalClientConectionLabel = totalClientConectionLabel;
    }
    
	@Override
	public String doInBackground(){
		startServer();
		
		return "none";
	}

    @Override
    protected void process(List<String> chunks) {

    	totalClientConectionLabel.setText("Client connections: " + chunks.get(0));
	}
    
	public boolean isListening() {
		return listening;
	}
	
	public void setListening(boolean listening) {
		this.listening = listening;
	}
	
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

	private void startServer() {
		listening = true;
		
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
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
