package com.ryanliang.knockknock;

import java.io.Serializable;

/**
 * KKServerResponse class defines the data structure for knock knock server response to the client. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKServerResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7298074464525158653L;
	private final KKJoke joke;
	private final String knockKnock = "Knock! Knock!";
	private final String whoIsThere = "Who's there?";
	private final String who = " who?";
	private final String suppose = "You're supposed to say \"";
	private final String tryAgain = "\"! Try again. Knock! Knock!"; 
	private final String wantMore = " Want another? (y/n)";

	/**
	 * This is the only constructor defined for this class.
	 * @param joke Is a KK joke
	 */
	public KKServerResponse(KKJoke joke){
		this.joke = joke;
	}

	/**
	 * This method returns the KK joke.
	 * @return KK joke
	 */
	public KKJoke getJoke() {
		return joke;
	}

	/**
	 * This method returns a custom string.
	 * @return a custom string
	 */
	public String getKnockKnock() {
		return knockKnock;
	}

	/**
	 * This method returns a custom string.
	 * @return a custom string
	 */
	public String getWhoIsThere() {
		return whoIsThere;
	}

	/**
	 * This method returns a custom string.
	 * @return a custom string
	 */
	public String getWho() {
		return who;
	}

	/**
	 * This method returns a custom string.
	 * @return a custom string
	 */
	public String getSuppose() {
		return suppose;
	}

	/**
	 * This method returns a custom string.
	 * @return a custom string
	 */
	public String getTryAgain() {
		return tryAgain;
	}
	
	/**
	 * This method returns a custom string.
	 * @return a custom string
	 */
	public String getWantMore() {
		return wantMore;
	}
	
}
