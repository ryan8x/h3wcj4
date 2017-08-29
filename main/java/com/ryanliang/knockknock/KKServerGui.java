package com.ryanliang.knockknock;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

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
	
	private final JPanel northPanel = new JPanel();
	private final JPanel centerPanel = new JPanel();
	private final JPanel southPanel = new JPanel();
	
	private int kkServerPort = 5555;
	private JTextField kkServerPortField = new JTextField(String.valueOf(kkServerPort));
	
	private String serverStarted = "Server status: Started";
	private String serverStopped = "Server status: Stopped";
	private JLabel serverStatusLabel = new JLabel("");
	
	private JLabel totalClientConectionLabel = new JLabel("");
	
	private BackgroundSocketListener task = null;
    
	/**
	 * This is the only constructor defined for this class.
	 */
	public KKServerGui(){
		
		super("Knock Knock Server");
				
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
		
		if (task != null){
			task.stopServer();
			task = null;
			serverStatusLabel.setText(serverStopped);
			totalClientConectionLabel.setText("Client connections: 0");
			
			startServerToolBarButton.setEnabled(true);
			stopServerToolBarButton.setEnabled(false);
		}

	}

	/**
	 * This method starts the server.
	 */
	private void startServer() {
		
		if (task == null){
			task = new BackgroundSocketListener(kkServerPort, serverStatusLabel, totalClientConectionLabel);
			task.execute();	
			serverStatusLabel.setText(serverStarted);
			totalClientConectionLabel.setText("Client connections: 0");
			
			startServerToolBarButton.setEnabled(false);
			stopServerToolBarButton.setEnabled(true);
		}
	}

	/**
	 * This method terminates the server app.
	 */
	private void quitApp() {
    	int answer = JOptionPane.showConfirmDialog(null, "Exit App?");
    	if (answer == JOptionPane.YES_OPTION){
    		stopServer(); 
    		System.exit(0);
    	}
    }

}
