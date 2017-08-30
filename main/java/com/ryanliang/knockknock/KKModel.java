package com.ryanliang.knockknock;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * KKModel implements KKModellable interface to get data from a source (such a file or database) or save data to a source. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public class KKModel implements KKModellable {
	private static Scanner input;
	private static List<KKJoke> kkJokeList = null;
	private static String delimiter = "###";	
	
	/**
	 * This is the only constructor defined for this class.
	 */
	public KKModel(){
		
		//Read data from file once only and share with other KKModel instances.  Note that kkJokeList is declared as static.
		if (kkJokeList == null){
			openFile();
			readFile();
			closeFile();
		}
	}
	
	/**
	 * This method is for opening a file. 
	 */
	private void openFile() {
		
		try {
			input = new Scanner(Paths.get("kk-jokes.txt"));
		} catch (IOException e) {
			System.err.println("Error opening file");
			}
		
	}

	/**
	 * This method is for obtaining data from a file. 
	 */
	private void readFile() {

		if (input != null){
			kkJokeList = new ArrayList<KKJoke>(50);
			String clue;
			String answer;
			String line;
			String [] lineParts;
			try {
				while (input.hasNextLine()){
					line = input.nextLine();
					lineParts = line.split(delimiter);	

					if (lineParts.length == 2){
						clue = lineParts[0].trim();
						answer = lineParts[1].trim();
						if (clue.length() > 0 && answer.length() > 0){
							kkJokeList.add(new KKJoke(clue, answer));
						}
					}
				}
			} 
			catch (NoSuchElementException e) {
				System.err.println("File improperly formed");
				//e.printStackTrace();
				kkJokeList = null;
			}
			catch (IllegalStateException e) {
				System.err.println("Error reading from file");
				kkJokeList = null;
			}
		}
	}

	/**
	 * This method is for freeing up resources. 
	 */
	private void closeFile() {
		if (input != null)
			input.close();
	}
	
	/**
	 * This method returns a duplicated copy of knock knock list of jokes.
	 */
	@Override
	public List<KKJoke> getListOfKKJokes() {
		
		List<KKJoke> copyList;
		
		if (kkJokeList != null){
			int listSize = kkJokeList.size();

				copyList = new ArrayList<KKJoke>(listSize);

				for (KKJoke joke : kkJokeList){
					copyList.add(joke);
				}
			
			if (listSize < 1){
				kkJokeList = null;
			}
		}
		else{
			//empty list
			copyList = new ArrayList<KKJoke>(0);
		}
		return copyList;
	}
}
