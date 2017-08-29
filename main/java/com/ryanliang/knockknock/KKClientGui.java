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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

/**
 * KKClientGui is a JFrame subclass defining the knock knock client app GUI.
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKClientGui extends JFrame {

	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenu helpMenu = new JMenu("Help");
	
	private final JMenuItem setupServerInfoFileMenu = new JMenuItem("Setup Server Info");
	private final JMenuItem connectFileMenu = new JMenuItem("Connect To Server");
	private final JMenuItem disconnectFileMenu = new JMenuItem("Disconnect");
	private final JMenuItem exitFileMenu = new JMenuItem("Exit");
	
	private final JMenuItem aboutHelpMenu = new JMenuItem("About");
	
	private final JToolBar toolBar = new JToolBar();
	private final JButton disconnectToolBarButton = new JButton("Disconnect"); 
	private final JButton connectToolBarButton = new JButton("Connect"); 
	
	private final JPanel northPanel = new JPanel();
	private final JPanel centerPanel = new JPanel();
	private final JPanel southPanel = new JPanel();
	
	private String kkServerHost = "localhost";
	private int kkServerPort = 5555;
	private JTextField kkServerHostField = new JTextField(kkServerHost);
	private JTextField kkServerPortField = new JTextField(String.valueOf(kkServerPort));
	
	private String online = "Connection status: Online";
	private String offline = "Connection status: Offline";
	private JLabel connectionStatusLabel = new JLabel("");
	
	private JTextArea chatTextArea = new JTextArea();
	private JTextField userInputTextField = new JTextField();
	
	private BackgroundSocketClient task = null;
	
	/**
	 * This is the only constructor defined for this class.
	 */
	public KKClientGui(){
		
		super("Knock Knock Client");
				
		organizeUI();
		addListeners();
			
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * This method arranges the layout of the GUI components. 
	 */
	private void organizeUI() {
		fileMenu.add(setupServerInfoFileMenu);
		fileMenu.add(connectFileMenu);
		fileMenu.add(disconnectFileMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitFileMenu);
		
		helpMenu.add(aboutHelpMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		
		toolBar.add(connectToolBarButton);
		toolBar.add(disconnectToolBarButton);
		
		connectionStatusLabel.setText(offline);
		northPanel.setLayout(new BorderLayout());
		northPanel.add(toolBar, BorderLayout.NORTH);
		northPanel.add(connectionStatusLabel, BorderLayout.WEST);
		add(northPanel, BorderLayout.NORTH);
		
		centerPanel.setLayout(new BorderLayout());
		add(centerPanel, BorderLayout.CENTER);
		chatTextArea.setEditable(false);
		centerPanel.add(new JScrollPane(chatTextArea));
		
		southPanel.setLayout(new BorderLayout());
		southPanel.add(userInputTextField);
		
		add(southPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * This method configures listeners of the GUI components. 
	 */
	private void addListeners() {
		setupServerInfoFileMenu.addActionListener(event -> setupServerInfo());		
		connectFileMenu.addActionListener(event -> connect());
		disconnectFileMenu.addActionListener(event -> disconnect());
		
		exitFileMenu.addActionListener(event -> quitApp());
	
		aboutHelpMenu.addActionListener(event -> {
			JOptionPane.showMessageDialog(null, "Knock Knock Client v1.0 Copyright 2017 RLTech Inc");
		});
			
		connectToolBarButton.addActionListener(event -> connect());
		disconnectToolBarButton.addActionListener(event -> disconnect());
		
		//Listen for ENTER key press.  This method detects ENTER key press by default.
		userInputTextField.addActionListener(event -> sendUserInput());

		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	quitApp();	
		    }
		});
	}

	/**
	 * This method enables user to specify the server's host name/ip address and port number.
	 */
	private void setupServerInfo() {
		   Object[] message = {
			   "Server Host Name or IP Address:", kkServerHostField,
		       "Server Port:", kkServerPortField,
		   };

		   int option = JOptionPane.showConfirmDialog(null, message, "Setup Server", JOptionPane.OK_CANCEL_OPTION);
		   if (option == JOptionPane.OK_OPTION) {
			   kkServerHost = kkServerHostField.getText().trim();
			   
			   String portStr = kkServerPortField.getText().trim();
			   if(Utility.isNumeric(portStr)){
				   int port = Integer.valueOf(portStr);
				   kkServerPort = (port > 0 && port < 65536?port:kkServerPort);
			   }
		   }
		   kkServerHostField.setText(kkServerHost);
		   kkServerPortField.setText(String.valueOf(kkServerPort));
	}
	
	/**
	 * This method handles the user chat input.
	 */
	private void sendUserInput() {
		String userInput = userInputTextField.getText().trim();
		userInputTextField.setText("");
		
		if (task != null && !userInput.equals("")){
			chatTextArea.append(userInput + "\n");
			task.processUserInput(userInput);
		}
	}

	/**
	 * This method ends the network connection.
	 */
	private void disconnect() {

		if (task != null){
			task.stopServer();
			task = null;
			connectionStatusLabel.setText(offline);
			disconnectToolBarButton.setEnabled(false);
			connectToolBarButton.setEnabled(true);
		}
	}

	/**
	 * This method starts the network connection.
	 */
	public void connect() {
		
		disconnect();
		task = new BackgroundSocketClient(kkServerHost, kkServerPort, connectionStatusLabel, chatTextArea);
		task.execute();	
		
		connectionStatusLabel.setText(online);
		connectToolBarButton.setEnabled(false);
		disconnectToolBarButton.setEnabled(true);
	}

	/**
	 * This method terminates the client app.
	 */
	private void quitApp() {
    	int answer = JOptionPane.showConfirmDialog(null, "Exit App?");
    	if (answer == JOptionPane.YES_OPTION){
    		disconnect(); 
    		System.exit(0);
    	}
    }

}
