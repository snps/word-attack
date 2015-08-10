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
	private List<Character> unavailLetters;

	public WordChooser(List<String> wordlist) {
		this.wordlist = wordlist;
		chosenWords = new ArrayList<String>();		
		unavailLetters = new ArrayList<Character>();
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
		while (n <= wordlist.size()-1) {
			String word = wordlist.get(n);
			int m = 0;
			while(m < chosenWords.size()) {
				if(isAvailable(word)) {
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

	private boolean isAvailable(String word) {
		char firstLetter = word.charAt(0);
		return unavailLetters.contains(firstLetter);
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
			unavailLetters.add(word.charAt(0));
			return word;
		} else {
			while(hasAvailableWord()) {
				if(!unavailLetters.contains(word.charAt(0))){
					chosenWords.add(word);
					unavailLetters.add(word.charAt(0));
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
		char letterToGiveBack= word.charAt(0);
		int index = unavailLetters.indexOf(letterToGiveBack);
		unavailLetters.remove(index);
	}
	
		
}
