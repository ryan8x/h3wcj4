package com.ryanliang.knockknock;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

/**
 * The knock knock app displays a new client GUI window.
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKClient {
	
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {

			KKClientGui kkClient = new KKClientGui();
			
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenWidth = (int) (screenSize.width*0.2);
			int screenHeight = (int) (screenSize.height*0.2);
			kkClient.setSize(screenWidth, screenHeight);

			kkClient.setLocationRelativeTo(null);
			kkClient.setVisible(true);
			kkClient.connect();
			
	});
		
	}

}
/*
-real time total client connections

-serialized network transmission
-thread pool

-display total jokes?
-load joke file via UI?
-Add jokes via UI?
*/