package com.ryanliang.knockknock;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadTextFile {
	
	private static Scanner input;
	
	public static void main(String[] args) {
		openFile();
		readRecords();
		closeFile();

	}

	private static void openFile() {
		
		try {
			input = new Scanner(Paths.get("clients.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void readRecords() {
		while (input.hasNext()){
			System.out.printf("%-10d%-12s%-12s%10.2f%n", input.nextInt(), input.next(), input.next(), input.nextDouble());
		}
		
	}

	private static void closeFile() {
		if (input != null)
			input.close();
		
	}

}
