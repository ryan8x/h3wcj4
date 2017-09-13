package com.ryanliang.knockknock;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

/**
 * KKServerGui is a JFrame subclass defining the knock knock server app GUI.
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKServerGui extends JFrame {
	
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenu helpMenu = new JMenu("Help");
	
	private final JMenuItem setupServerInfoFileMenu = new JMenuItem("Setup Server Info");
	private final JMenuItem startServerFileMenu = new JMenuItem("Start Server");
	private final JMenuItem stopServerFileMenu = new JMenuItem("Stop Server");
	private final JMenuItem exitFileMenu = new JMenuItem("Exit");
	
	private final JMenuItem aboutHelpMenu = new JMenuItem("About");
	
	private final JToolBar toolBar = new JToolBar();
	private final JButton startServerToolBarButton = new JButton("Start Server"); 
	private final JButton stopServerToolBarButton = new JButton("Stop Server"); 
	private final JButton kkClientToolBarButton = new JButton("Launch Client"); 
	
	private final JPanel northPanel = new JPanel();
	private final JPanel centerPanel = new JPanel();
	private final JPanel southPanel = new JPanel();
	
	private int kkServerPort = 5555;
	private JTextField kkServerPortField;
	
	private final String serverStarted = "<html>Server status: <font color='green'> Started</font></html>";
	private final String serverStopped = "<html>Server status: <font color='blue'> Stopped</font></html>";
	private final String jokesNotFound = "<html>Server status: <font color='red'>Jokes not found or missing </font> the <font color='blue'>kk-jokes.txt</font> file which must be stored in the same path as this server app.  <br>"
			+ "Each line or joke in the <font color='blue'>kk-jokes.txt </font>file must also be formatted as <font color='blue'>clue ### answer</font>.</html>";
	
	private JLabel serverStatusLabel = new JLabel("");
	
	private JLabel totalClientConectionLabel = new JLabel("");
	
	private BackgroundSocketListener socketListeningTask = null;
	private BackgroundConnectionCheck connectionCheckingTask = null;
    
	/**
	 * This is the only constructor defined for this class.
	 */
	public KKServerGui(){
		
		super("Knock Knock Server");
				
		loadData();
		organizeUI();
		addListeners();
			
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * This method arranges the layout of the GUI components. 
	 */
	private void organizeUI() {
		fileMenu.add(setupServerInfoFileMenu);
		fileMenu.add(startServerFileMenu);
		fileMenu.add(stopServerFileMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitFileMenu);

		helpMenu.add(aboutHelpMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		
		toolBar.add(startServerToolBarButton);
		toolBar.add(stopServerToolBarButton);
		stopServerToolBarButton.setEnabled(false);
		toolBar.addSeparator();
		toolBar.add(kkClientToolBarButton);
		
		kkServerPortField = new JTextField(String.valueOf(kkServerPort));
		
		serverStatusLabel.setText(serverStopped);
		northPanel.setLayout(new BorderLayout());
		northPanel.add(toolBar, BorderLayout.NORTH);
		northPanel.add(serverStatusLabel, BorderLayout.WEST);
		add(northPanel, BorderLayout.NORTH);
		
		centerPanel.setLayout(new BorderLayout());
		add(centerPanel, BorderLayout.CENTER);
		
		southPanel.setLayout(new BorderLayout());
		southPanel.add(totalClientConectionLabel, BorderLayout.WEST);
		
		add(southPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * This method configures listeners of the GUI components. 
	 */
	private void addListeners() {
		setupServerInfoFileMenu.addActionListener(event -> setupServerInfo());
		startServerFileMenu.addActionListener(event -> startServer());
		stopServerFileMenu.addActionListener(event -> stopServer());
		
		exitFileMenu.addActionListener(event -> quitApp());
	
		aboutHelpMenu.addActionListener(event -> {
			JOptionPane.showMessageDialog(null, "Knock Knock Server v1.0 Copyright 2017 RLTech Inc");
		});
		
		startServerToolBarButton.addActionListener(event -> {
			startServer();
			startServerToolBarButton.setEnabled(false);
			stopServerToolBarButton.setEnabled(true);
		});
		
		stopServerToolBarButton.addActionListener(event -> {
			stopServer();
			startServerToolBarButton.setEnabled(true);
			stopServerToolBarButton.setEnabled(false);
		});

		kkClientToolBarButton.addActionListener(event -> {

			try {
				Runtime.getRuntime().exec("java -jar KKClient.jar");
			} catch (IOException e1) {
				e1.printStackTrace();
				Utility.displayErrorMessage("Fail to find or launch KKClient.jar client app.  Make sure Java run time (JRE) is installed and KKClient.jar is stored in the same path as this server app.");
			}
		});
		
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	quitApp();	
		    }
		});
	}

	/**
	 * This method enables user to specify the server's port number.
	 */
	private void setupServerInfo() {
		   Object[] message = {
		       "Server Port:", kkServerPortField,
		   };

		   int option = JOptionPane.showConfirmDialog(null, message, "Setup Server", JOptionPane.OK_CANCEL_OPTION);
		   if (option == JOptionPane.OK_OPTION) {		   
			   String portStr = kkServerPortField.getText().trim();
			   if(Utility.isNumeric(portStr)){
				   int port = Integer.valueOf(portStr);
				   kkServerPort = (port > 0 && port < 65536?port:kkServerPort);
			   }
		   }
		   kkServerPortField.setText(String.valueOf(kkServerPort));
	}
	
	/**
	 * This method stops the server.
	 */
	private void stopServer() {

		if (socketListeningTask != null){
			socketListeningTask.stopServer();
			
			try {
				//Sleep time is needed to prevent connection counter being reset too soon before socketListeningTask.stopServer() is completed.  Sleep time may need to be increased if not enough.
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.err.println("thread sleep method of KKServerGUI is being interrupted.");
			}
			
			ConnectionCounter.resetConnectionCounter();
			socketListeningTask = null;
			serverStatusLabel.setText(serverStopped);
			totalClientConectionLabel.setText("Client connections: 0");

			startServerToolBarButton.setEnabled(true);
			stopServerToolBarButton.setEnabled(false);

			connectionCheckingTask.stopCheckingConnection();
		}
	}

	/**
	 * This method starts the server.
	 */
	private void startServer() {

		KKModellable model = new KKModel();
		List<KKJoke> kkJokeList = model.getListOfKKJokes();
		
		if (kkJokeList.size() > 0){
			if (socketListeningTask == null){
				socketListeningTask = new BackgroundSocketListener(kkServerPort, serverStatusLabel);
				socketListeningTask.execute();
				ConnectionCounter.resetConnectionCounter();
				serverStatusLabel.setText(serverStarted);
				totalClientConectionLabel.setText("Client connections: 0");

				startServerToolBarButton.setEnabled(false);
				stopServerToolBarButton.setEnabled(true);
				
				connectionCheckingTask = new BackgroundConnectionCheck(totalClientConectionLabel);
				connectionCheckingTask.execute();
			}
		}
		else{
			//Do not start server because joke list is empty
			serverStatusLabel.setText(jokesNotFound);
			Utility.displayErrorMessage("Jokes not found or missing the \" kk-jokes.txt \" file which must be stored in the same path as this server app.  "
			+ "Each line or joke in the \" kk-jokes.txt \" file must also be formatted as \" clue ### answer \" without the quotes.");
		}
	}

	/**
	 * This method terminates the server app.
	 */
	private void quitApp() {
    	int answer = JOptionPane.showConfirmDialog(null, "Exit App?");
    	if (answer == JOptionPane.YES_OPTION){
    		stopServer(); 
    		saveData();
    		System.exit(0);
    	}
    }

	/**
	 * This method saves the server port info to a file.
	 */
	private void saveData() {
		ObjectOutputStream out = null;
		try {
			FileOutputStream fileOut = new FileOutputStream("server-info.dat");
			out = new ObjectOutputStream(fileOut);
			out.writeObject(kkServerPort);
			out.flush();
		} catch (FileNotFoundException e) {
			System.err.println("saving server-info.dat file is encountering an error for some reason.");	
		} catch (IOException e) {
			System.err.println("saving server-info.dat file is encountering an error for some reason.");	
		} 
		finally {
			if (out != null){
				try{
					out.close();
				}
				catch (IOException e){
					System.err.println("saving server-info.dat file is encountering an error for some reason.");	
				}
			}
		}
	}
	
	/**
	 * This method restores the server port info from a file.
	 */
	private void loadData() {

		try {
			FileInputStream fileIn = new FileInputStream("server-info.dat");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			kkServerPort = (int) in.readObject();
			in.close();
		} catch (Exception e) {
			kkServerPort = 5555;
			//e.printStackTrace();
			System.err.println("server-info.dat file is likely missing but it should be created automatically when this app is closed.");
		}	
	}

}
