package com.ryanliang.knockknock;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class KKModel implements KKModellable {
	private static Scanner input;
	private List<KKJoke> kkJokeList = new ArrayList<KKJoke>(50);
	private static String delimiter = "###";	
	
	public KKModel(){
		openFile();
		readFile();
		closeFile();
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
		}
		catch (IllegalStateException e) {
			System.err.println("Error reading from file");
		}
	}

	private void closeFile() {
		if (input != null)
			input.close();
	}
	
	@Override
	public List<KKJoke> getData() {
		
		return kkJokeList;	
	}
}
