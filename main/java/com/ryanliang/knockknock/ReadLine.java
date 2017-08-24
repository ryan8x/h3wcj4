package com.ryanliang.knockknock;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReadLine {
	
	private static Scanner input;
	private static ArrayList<KKJoke> kkJokeList = new ArrayList<KKJoke>(100);
	private static String delimiter = "###";	
	
	public static void main(String[] args) {
		openFile();
		readRecords();
		closeFile();
		displayJokes();

	}

	private static void displayJokes() {
		
		Collections.shuffle(kkJokeList);
		for (KKJoke joke : kkJokeList){
			System.out.println(joke.getClue());
			System.out.println(joke.getAnswer());
		}
		
	}

	private static void openFile() {
		
		try {
			input = new Scanner(Paths.get("clients.txt"));
		} catch (IOException e) {
			System.err.println("Error opening file");
			//e.printStackTrace();
		}
		
	}

	private static void readRecords() {
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

	private static void closeFile() {
		if (input != null)
			input.close();
		
	}

}
