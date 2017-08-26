package com.ryanliang.knockknock;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class KKModel implements KKModellable {
	private static Scanner input;
	private static List<KKJoke> kkJokeList = null;
	private static String delimiter = "###";	
	
	public KKModel(){
		
		//Read data from file once only and share with other KKModel instances.  Note that kkJokeList is declared as static.
		if (kkJokeList == null){
			openFile();
			readFile();
			closeFile();
		}
	}
	private void openFile() {
		
		try {
			input = new Scanner(Paths.get("clients.txt"));
		} catch (IOException e) {
			System.err.println("Error opening file");
			//e.printStackTrace();
		}
	}

	private void readFile() {
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
		} catch (NoSuchElementException e) {
			System.err.println("File improperly formed");
			//e.printStackTrace();
			kkJokeList = null;
		}
		catch (IllegalStateException e) {
			System.err.println("Error reading from file");
			kkJokeList = null;
		}
	}

	private void closeFile() {
		if (input != null)
			input.close();
	}
	
	@Override
	public List<KKJoke> getListOfKKJokes() {
		
		List<KKJoke> copyList = new ArrayList<KKJoke>(kkJokeList.size());
		
		for (KKJoke joke : kkJokeList){
			copyList.add(joke);
		}
		
		//return a new duplicated copy instead of the original reference static list.
		return copyList;	
	}
}
