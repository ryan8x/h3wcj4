package com.ryanliang.knockknock;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class BackgroundConnectionCheck extends SwingWorker<Void, String> {
    private List<KKMultiServerThread> socketThreadList;
	private JLabel totalClientConectionLabel;
    private boolean runLoop = false;
    
    public BackgroundConnectionCheck(List<KKMultiServerThread> socketThreadList, JLabel totalClientConectionLabel){
    	this.socketThreadList = socketThreadList;
    	this.totalClientConectionLabel = totalClientConectionLabel;
    }

	@Override
	protected Void doInBackground() throws Exception {

		runLoop = true;

		while (runLoop){
			Thread.sleep(1000);
			for (KKMultiServerThread kk : socketThreadList){
				if (kk.checkConnection() == false){
					socketThreadList.remove(kk);
				}
			}
			publish(String.valueOf(socketThreadList.size()));
		}
		
		return null;
	}
	
    @Override
    protected void process(List<String> chunks) {

    	totalClientConectionLabel.setText("Client connections: " + chunks.get(0));
	}
    
	public void stopChecking(){
		runLoop = false;
	}
}
