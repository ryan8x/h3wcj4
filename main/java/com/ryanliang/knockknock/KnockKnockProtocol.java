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

	private KKProtocolState state = KKProtocolState.WAITING;
	private int currentJoke = 0;
	
	private KKModellable model;
	private List<KKJoke> kkJokeList;
	
	private final String whoIsThere = "Who's there?";

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
	public String processInput(String theInput) {
		String theOutput = null;

		if (state == KKProtocolState.WAITING) {
			theOutput = "Knock! Knock!";
			state = KKProtocolState.SENTKNOCKKNOCK;
		} else if (state == KKProtocolState.SENTKNOCKKNOCK) {
			if (theInput.equalsIgnoreCase(whoIsThere)) {
				theOutput = kkJokeList.get(currentJoke).getClue();
				state = KKProtocolState.SENTCLUE;
			} else {
				theOutput = "You're supposed to say \"" + whoIsThere + "\"! " +
						"Try again. Knock! Knock!";
			}
		} else if (state == KKProtocolState.SENTCLUE) {
			if (theInput.equalsIgnoreCase(kkJokeList.get(currentJoke).getClue() + " who?")) {
				theOutput = kkJokeList.get(currentJoke).getAnswer() + " Want another? (y/n)";
				state = KKProtocolState.ANOTHER;
			} else {
				theOutput = "You're supposed to say \"" + 
						kkJokeList.get(currentJoke).getClue() + 
						" who?\"" + 
						"! Try again. Knock! Knock!";
				state = KKProtocolState.SENTKNOCKKNOCK;
			}
		} else if (state == KKProtocolState.ANOTHER) {
			if (theInput.equalsIgnoreCase("y")) {
				theOutput = "Knock! Knock!";
				if (currentJoke == (NUMJOKES - 1))
					currentJoke = 0;
				else
					currentJoke++;
				state = KKProtocolState.SENTKNOCKKNOCK;
			} else {
				theOutput = "Bye";
				state = KKProtocolState.WAITING;
			}
		}
		return theOutput;
	}
}
