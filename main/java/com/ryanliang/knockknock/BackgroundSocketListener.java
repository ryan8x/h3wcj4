package com.ryanliang.knockknock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import javax.swing.SwingWorker;

public class BackgroundSocketListener extends SwingWorker<Boolean, Object> {
	
    private ServerSocket serverSocket = null;
    private boolean listening = false;
    
    public BackgroundSocketListener(Boolean listening, ServerSocket serverSocket){
    	//this.listening = listening;
    	//this.serverSocket = serverSocket;
    }
	@Override
	public Boolean doInBackground(){
		startServer();
		
		return true;
	}

	public boolean isListening() {
		return listening;
	}
	
	public void setListening(boolean listening) {
		this.listening = listening;
	}
	
	public void stopServer() {
		System.out.println("stop");
		try {
			if (serverSocket != null){
				serverSocket.close();
				serverSocket = null;
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
				new KKMultiServerThread(serverSocket.accept()).start();
			}
		}
        catch (IOException e) {
            e.printStackTrace();
		}
	}
}
