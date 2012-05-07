package wordlist.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import wordlist.WordFileReader;

public class TestWordFileReader {
	private static final String WORD_FILE = "words/words1.txt";

	@Test
	public void testCreateWordFileReader() {
		WordFileReader reader = null;

		try {
			reader = new WordFileReader(WORD_FILE);
		} catch (FileNotFoundException e) {
			// Nothing.
		}

		assertTrue(reader != null);
	}

	@Test
	public void testReadWordFile() {
		String[] correctWords = { "elaking", "tuffing", "cowboy", "gangster" };

		WordFileReader reader = null;

		try {
			reader = new WordFileReader(WORD_FILE);
		} catch (FileNotFoundException e) {
			// Nothing.
		}

		List<String> words = reader.readWords();

		assertTrue(!words.isEmpty());

		for (int i = 0, n = words.size(); i < n; i++) {
			String word = words.get(i);
			assertEquals(correctWords[i], word);
			System.out.println(word);
		}
	}
}
