package com.ryanliang.knockknock;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

/**
 * The knock knock app displays a new server GUI window.
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKServer2 {

	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {

			KKServerGui kkServer = new KKServerGui();
			
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenWidth = (int) (screenSize.width*0.2);
			int screenHeight = (int) (screenSize.height*0.2);
			kkServer.setSize(screenWidth, screenHeight);

			kkServer.setLocationRelativeTo(null);
			kkServer.setVisible(true);
			
	});
		
	}

}
