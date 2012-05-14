package wordlist.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import wordlist.WordChooser;
import wordlist.WordFileReader;

public class TestWordChooser {
	public static final String WORD_FILE_1 = "words/words1.txt";
	public static final String WORD_FILE_2 = "words/words2.txt";
	public static final String WORD_FILE_3 = "words/words3.txt";

	private List<String> words1;
	private List<String> words2;
	private List<String> words3;

	@Before
	public void setUp() {
		// Read word lists from word files.
		words1 = getWordlistFromFile(WORD_FILE_1);
		words2 = getWordlistFromFile(WORD_FILE_2);
		words3 = getWordlistFromFile(WORD_FILE_3);
	}

	@Test
	public void testCreateWordChooser() {
		WordChooser wc = new WordChooser(words1);
		assertTrue("No available words", wc.hasAvailableWord());
	}

	@Test
	public void testGetWordsWithDifferentLetters() {
		WordChooser wc = new WordChooser(words1);
		assertTrue("No available words", wc.hasAvailableWord());

		HashSet<Character> letters = new HashSet<Character>();

		while (wc.hasAvailableWord()) {
			String word = wc.getNextWord();
			assertFalse("Word was null", word == null);

			char firstLetter = word.charAt(0);
			assertFalse("Gave word with same first letter twice", letters.contains(firstLetter));

			letters.add(firstLetter);
		}
	}

	@Test
	public void testGiveBackWord() {
		WordChooser wc = new WordChooser(words1);

		String word = "";
		for (int i = 0; i < 4; i++) {
			word = wc.getNextWord();
		}

		assertFalse("Gave more words then possible", wc.hasAvailableWord());

		wc.giveBackWord(word);
		assertTrue("No words available although a word was given back", wc.hasAvailableWord());
		assertFalse("Word was null", wc.getNextWord() == null);
	}

	@Test
	public void testGetCorrectNbrOfWords() {
		WordChooser wc = new WordChooser(words2);

		int nbr = 0;

		while (wc.hasAvailableWord()) {
			wc.getNextWord();
			nbr++;
		}

		assertEquals("Gave wrong number of words", 2, nbr);
	}
	
	@Test
	public void testGetCorrectNbrOfWordsFromLongList() {
		WordChooser wc = new WordChooser(words3);

		int nbr = 0;

		while (wc.hasAvailableWord()) {
			wc.getNextWord();
			nbr++;
		}

		assertEquals("Gave wrong number of words", 24, nbr);
	}
	
	@Test
	public void testGiveBackWordWithLongList() {
		WordChooser wc = new WordChooser(words3);

		String word = "";
		for (int i = 0; i < 24; i++) {
			word = wc.getNextWord();
		}

		assertFalse("Gave more words then possible", wc.hasAvailableWord());

		wc.giveBackWord(word);
		assertTrue("No words available although a word was given back", wc.hasAvailableWord());
		assertFalse("Word was null", wc.getNextWord() == null);
	}
	
	private List<String> getWordlistFromFile(String fileName) {
		WordFileReader wordReader = null;
		try {
			wordReader = new WordFileReader(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("File not found " + fileName);
			return null;
		}
		return wordReader.readWords();
	}
}
