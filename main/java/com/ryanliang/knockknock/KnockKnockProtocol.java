package com.ryanliang.knockknock;

import java.net.*;
import java.util.Collections;
import java.util.List;
import java.io.*;

/**
 * KnockKnockProtocol class defines the customized network communication protocols between the server and the client. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KnockKnockProtocol{

	private int NUMJOKES;

	private int currentJoke = 0;
	
	private KKModellable model;
	private List<KKJoke> kkJokeList;

	/**
	 * This is the only constructor defined for this class.
	 */
	public KnockKnockProtocol(){
		getKKJokes();
	}
	
	/**
	 * This method obtain the list of jokes.
	 */
	private void getKKJokes() {
		model = new KKModel();
		kkJokeList = model.getListOfKKJokes();
		NUMJOKES = kkJokeList.size();
		Collections.shuffle(kkJokeList);
	}

	/**
	 * This method process the customized network communication protocols between the server and the client.
	 */
	public KKJoke processInput() {
		KKJoke joke = kkJokeList.get(currentJoke);
		if (currentJoke == (NUMJOKES - 1)){
			currentJoke = 0;
		}
		else{
			currentJoke++;
		}
		
		return joke;
	}
}
