/**
 *
 * @author Ryan L.
 */


package com.ryanliang.knockknock;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

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
			
	});
		
	}

}
