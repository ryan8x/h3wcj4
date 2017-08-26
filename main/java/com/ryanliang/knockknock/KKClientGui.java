/**
 *
 * @author Ryan L.
 */

package com.ryanliang.knockknock;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class KKClientGui extends JFrame {

	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenu editMenu = new JMenu("Edit");
	private final JMenu helpMenu = new JMenu("Help");
	
	private final JMenuItem startServerFileMenu = new JMenuItem("Start Server");
	private final JMenuItem stopServerFileMenu = new JMenuItem("Stop Server");
	private final JMenuItem exitFileMenu = new JMenuItem("Exit");
	
	private final JMenuItem newEditMenu = new JMenuItem("New");
	
	private final JMenuItem aboutHelpMenu = new JMenuItem("About");
	
	private final JPanel northPanel = new JPanel();
	private final JPanel centerPanel = new JPanel();
	private final JPanel southPanel = new JPanel();
	
	private String online = "Connection status: Online";
	private String offline = "Connection status: Offline";
	private JLabel connectionStatusLabel = new JLabel("");
	
	private JTextArea chatTextArea = new JTextArea();
	private JTextField userInputTextField = new JTextField();
	
	private BackgroundSocketClient task = null;
	
	public KKClientGui(){
		
		super("Knock Knock Client");
				
		organizeUI();
		addListeners();
			
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	private void organizeUI() {
		fileMenu.add(startServerFileMenu);
		fileMenu.add(stopServerFileMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitFileMenu);
		
		editMenu.add(newEditMenu);
		
		helpMenu.add(aboutHelpMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		
		connectionStatusLabel.setText(offline);
		northPanel.setLayout(new BorderLayout());
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
	
	private void addListeners() {
		startServerFileMenu.addActionListener(event -> startClient());
		stopServerFileMenu.addActionListener(event -> stopClient());
		exitFileMenu.addActionListener(event -> quitApp());
	
		aboutHelpMenu.addActionListener(event -> {
			JOptionPane.showMessageDialog(null, "Knock Knock Client v1.0 Copyright 2017 RLTech Inc");
		});
			
		//Listen for ENTER key press.  This method detects ENTER key press by default.
		userInputTextField.addActionListener(event -> sendUserInput());

		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	quitApp();	
		    }
		});
	}

	private void sendUserInput() {
		String userInput = userInputTextField.getText().trim();
		userInputTextField.setText("");
		
		if (task != null && !userInput.equals("")){
			chatTextArea.append(userInput + "\n");
			task.processUserInput(userInput);
		}
	}

	private void stopClient() {

		if (task != null){
			task.stopServer();
			task = null;
			connectionStatusLabel.setText(offline);
		}
	}

	private void startClient() {
		
		if (task == null){
			task = new BackgroundSocketClient(chatTextArea);
			task.execute();	
			connectionStatusLabel.setText(online);
		}
	}

	private void quitApp() {
    	int answer = JOptionPane.showConfirmDialog(null, "Exit App?");
    	if (answer == JOptionPane.YES_OPTION){
    		stopClient(); 
    		System.exit(0);
    	}
    }

}
