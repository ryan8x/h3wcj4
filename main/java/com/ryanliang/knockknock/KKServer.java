/**
 *
 * @author Ryan L.
 */


package com.ryanliang.knockknock;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

public class KKServer {

	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {

			KKServerGui kkServer = new KKServerGui();
			
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenWidth = (int) (screenSize.width*0.8);
			int screenHeight = (int) (screenSize.height*0.8);
			kkServer.setSize(screenWidth, screenHeight);

			kkServer.setLocationRelativeTo(null);
			kkServer.setVisible(true);
			
	});
		
	}

}
