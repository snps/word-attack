package wordlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private List<String> chosenWords;

	public WordChooser(List<String> wordlist) {
		this.wordlist = wordlist;
		chosenWords = new ArrayList<String>();
	}

	public boolean hasAvailableWord() {
	
		if(wordlist.isEmpty()){
			return false;
		}
		if(chosenWords.isEmpty() && !wordlist.isEmpty()){
			return true;
		}
		boolean wordOk = false;
		int n = 0;
		while (n < wordlist.size()-1) {
			String word = wordlist.get(n);
			int m = 0;
			while(m < chosenWords.size()-1) {
				if(word.charAt(0) == chosenWords.get(m).charAt(0)) {
					wordOk = false;
					break;
				} 
				m++;
				wordOk = true;
			}
			if(wordOk) {
				return true;
			}
			n++;
		}
		return false;
	}

	public String getNextWord() {	
		if(wordlist.isEmpty()) {
			return null;
		}
		Random rand = new Random();
		int index = rand.nextInt(wordlist.size());
		String word = wordlist.get(index);
		if(chosenWords.isEmpty()) {
			chosenWords.add(word);
			return word;
		} else {
			while(hasAvailableWord()) {
				if(chosenWords.get(index).charAt(0) == word.charAt(0)){
					chosenWords.add(word);
					return word;
				} else {
					index = rand.nextInt(wordlist.size());
					word = wordlist.get(index);
				}
			}
		}
		return null;
	}

	public void giveBackWord(String word) {
		chosenWords.remove(word);
	}
		
}
