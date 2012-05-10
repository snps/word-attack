package wordlist;

import java.util.List;

/**
 * <p>
 * Implementation of a word chooser that chooses words from a word list.
 * </p>
 * <p>
 * Words are chosen in random order from the word list. The word chooser
 * guarantees that all given words has a different first letter.
 * </p>
 */
public class WordChooser {
	private List<String> wordlist;

	public WordChooser(List<String> wordlist) {
		this.wordlist = wordlist;
	}

	public boolean hasAvailableWord() {
		// FIXME
		return false;
	}

	public String getNextWord() {
		// FIXME
		return null;
	}

	public void giveBackWord(String word) {
		// TODO
	}
}
