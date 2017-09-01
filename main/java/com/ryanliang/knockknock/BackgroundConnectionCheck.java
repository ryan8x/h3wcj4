package com.ryanliang.knockknock;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 * BackgroundConnectionCheck is a SwingWorker subclass utilized by the knock knock server app for obtaining the current total number of client socket connections. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class BackgroundConnectionCheck extends SwingWorker<Void, String> {
    private List<KKMultiServerThread> socketThreadList;
	private JLabel totalClientConectionLabel;
    private boolean runLoop = false;
    
	/**
	 * This is the only constructor defined for this class.
	 * @param socketThreadList List of KKMultiServerThread objects
	 * @param totalClientConectionLabel A reference JLabel for updating current total number of client socket connections
	 */
    public BackgroundConnectionCheck(List<KKMultiServerThread> socketThreadList, JLabel totalClientConectionLabel){
    	this.socketThreadList = socketThreadList;
    	this.totalClientConectionLabel = totalClientConectionLabel;
    }

	/**
	 * This method performs a task in the background in a SwingWorker thread.
	 * @return null  
	 */
	@Override
	protected Void doInBackground() throws Exception {

		runLoop = true;

		while (runLoop){
			Thread.sleep(1000);
			for (KKMultiServerThread kk : socketThreadList){
				if (!kk.isSocketAlive()){
					socketThreadList.remove(kk);
				}
			}
			publish(String.valueOf(socketThreadList.size()));
		}
		
		return null;
	}
	
	/**
	 * This method updates specific Swing components (UI) while doInBackground() is in progress. 
	 */
    @Override
    protected void process(List<String> chunks) {

    	totalClientConectionLabel.setText("Client connections: " + chunks.get(0));
	}
    
	/**
	 * This method stops the SwingwWorker execution. 
	 */
	public void stopCheckingConnection(){
		runLoop = false;
	}
}
