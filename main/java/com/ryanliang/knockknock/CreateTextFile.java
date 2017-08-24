package com.ryanliang.knockknock;

import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

public class CreateTextFile {
	private static Formatter output;
	public static void main(String[] args) {
		openFile();
		addRecords();
		closeFile();

	}
	public static void closeFile() {
		if (output != null)
			output.close();
		
	}
	public static void addRecords() {
		Scanner input = new Scanner(System.in);
		System.out.printf("%s%n%s%n? ", "enter", "enter");
		
		while (input.hasNext()){
			output.format("%d %s %s %.2f%n", input.nextInt(), input.next(), input.next(), input.nextDouble());
		}
	}
	public static void openFile() {
		try {
			output = new Formatter("clients.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
