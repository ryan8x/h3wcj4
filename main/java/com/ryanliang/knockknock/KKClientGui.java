/**
 *
 * @author Ryan L.
 */

package com.ryanliang.knockknock;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

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
	
	private final JButton newToolBarButton = new JButton("New"); 
	
	private final JToolBar toolBar = new JToolBar();
	private final JPanel westPanel = new JPanel();
	private final JPanel centerPanel = new JPanel();
	private final JPanel statusPanel = new JPanel();
	
	private final JLabel searchResultLabel = new JLabel("Search result: ");
	private JLabel searchResultStatus = new JLabel("");
	
	private JLabel serverResponseLabel = new JLabel("");
	private JTextArea userInputTextArea = new JTextArea();
	private JButton sendButton = new JButton("Send");
	
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
		
		toolBar.add(newToolBarButton);

		add(toolBar, BorderLayout.NORTH);
		add(westPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		westPanel.add(new JLabel("      "));
		
		centerPanel.setLayout(new GridLayout(0,1));
		centerPanel.add(serverResponseLabel);
		centerPanel.add(userInputTextArea);
		centerPanel.add(sendButton);
		
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.add(searchResultLabel);
		statusPanel.add(searchResultStatus);
	}
	
	private void addListeners() {
		startServerFileMenu.addActionListener(event -> startClient());
		stopServerFileMenu.addActionListener(event -> stopClient());
		exitFileMenu.addActionListener(event -> quitApp());
	
		aboutHelpMenu.addActionListener(event -> {
			JOptionPane.showMessageDialog(null, "Knock Knock Client v1.0 Copyright 2017 RLTech Inc");
		});
		
		sendButton.addActionListener(event -> sendUserInput());
		
		
		//newEditMenu.addActionListener(event -> newItem());
		
	
		//newToolBarButton.addActionListener(event -> newItem());

		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	quitApp();	
		    }
		});
	}

	private void sendUserInput() {
		String userInput = userInputTextArea.getText().trim();
		userInputTextArea.setText("");
		
		if (task != null && !userInput.equals("")){
			task.processUserInput(userInput);
		}
	}

	private void stopClient() {

		if (task != null){
			task.stopServer();
			task = null;
		}
	}

	private void startClient() {
		
		if (task == null){
			task = new BackgroundSocketClient(serverResponseLabel);
			task.execute();	
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
