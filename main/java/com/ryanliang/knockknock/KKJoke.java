package com.ryanliang.knockknock;

/**
 * KKJoke class defines the data structure for knock knock joke's clue and answer. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKJoke {

	private final String clue;
	private final String answer;
	
	/**
	 * This is the only constructor defined for this class.
	 * @param clue Is a KK joke's clue
	 * @param answer Is a KK joke's answer
	 */
	public KKJoke(String clue, String answer){
		this.clue = clue;
		this.answer = answer;
	}

	/**
	 * This method returns the KK joke's clue.
	 * @return KK joke's clue
	 */
	public String getClue() {
		return clue;
	}

	/**
	 * This method returns the KK joke's answer.
	 * @return KK joke's answer
	 */
	public String getAnswer() {
		return answer;
	}

}
