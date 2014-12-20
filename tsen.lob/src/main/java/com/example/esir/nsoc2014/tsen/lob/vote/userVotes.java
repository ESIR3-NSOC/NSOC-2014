package com.example.esir.nsoc2014.tsen.lob.vote;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class userVotes {
	public static void main(String[] args) {
		System.out.println("test");
	}

	public static boolean CalculVote(String id, double humidity_ext,
			double temp_ext, double temp_courant, String vote) {
		double addValue = Double.parseDouble(findValueVote(vote));

		return true;
	}

	private static String findValueVote(String vote) {
		try {
			FileReader fileToRead = new FileReader("./data/confData.txt");
			BufferedReader bf = new BufferedReader(fileToRead);
			String aLine;
			while ((aLine = bf.readLine()) != null) {
				if (aLine.contains(vote))
					break;
			}
			bf.close();
			if (aLine != null) {
				String[] dataVote = aLine.split(":");
				return dataVote[1];
			} else
				return "failed";
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "failed";
	}
}
