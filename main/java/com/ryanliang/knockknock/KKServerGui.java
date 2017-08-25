/**
 *
 * @author Ryan L.
 */

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
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

public class KKServerGui extends JFrame {
	
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
	private final JPanel statusPanel = new JPanel();
	
	private final JLabel searchResultLabel = new JLabel("Search result: ");
	private JLabel searchResultStatus = new JLabel("");
	
    //private ServerSocket serverSocket = null;
    //private boolean listening = false;
	
	private BackgroundSocketListener task = null;
    
	public KKServerGui(){
		
		super("Knock Knock Server");
				
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
				
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.add(searchResultLabel);
		statusPanel.add(searchResultStatus);
	}
	
	private void addListeners() {
		startServerFileMenu.addActionListener(event -> startServer());
		stopServerFileMenu.addActionListener(event -> stopServer());
		exitFileMenu.addActionListener(event -> quitApp());
	
		aboutHelpMenu.addActionListener(event -> {
			JOptionPane.showMessageDialog(null, "Knock Knock Server v1.0 Copyright 2017 RLTech Inc");
		});
		
		//newEditMenu.addActionListener(event -> newItem());
		
	
		//newToolBarButton.addActionListener(event -> newItem());

		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	quitApp();	
		    }
		});
	}

	private void stopServer() {
		
		if (task != null){
			task.stopServer();
			task = null;
		}

	}

	private void startServer() {
		
		if (task == null){
			task = new BackgroundSocketListener();
			task.execute();	
		}
	}

	private void quitApp() {
    	int answer = JOptionPane.showConfirmDialog(null, "Exit App?");
    	if (answer == JOptionPane.YES_OPTION){
    		stopServer(); 
    		System.exit(0);
    	}
    }

}
