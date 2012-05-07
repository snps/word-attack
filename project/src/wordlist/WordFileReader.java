package wordlist;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordFileReader {
	private FileReader fileReader;

	public WordFileReader(String fileName) throws FileNotFoundException {
		try {
			fileReader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + fileName);
			throw e;
		}
	}

	public List<String> readWords() {
		List<String> wordList = new ArrayList<String>();

		BufferedReader br = null;

		br = new BufferedReader(fileReader);

		String line;
		try {
			while ((line = br.readLine()) != null) {
				wordList.add(line);
			}
		} catch (IOException e) {
			System.err.println("I/O error when reading file.");

			return null;
		}

		return wordList;
	}
}
