package edu.usfca.cs272.lectures.basics.data;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Demonstrates how collections can be nested.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
public class NestedMapDemo {
	/**
	 * Test case for this demo. See Wikipedia for more.
	 *
	 * @see <a href="http://en.wikipedia.org/wiki/Time_flies_like_an_arrow;_fruit_flies_like_a_banana">Wikipedia</a>
	 */
	public static final String text = "time flies like an arrow fruit flies like a banana";

	/**
	 * Stores a set of words by the length of that word. For example, the word "ant"
	 * has 3 characters. The word "bat" also has length 3. So both words should
	 * appear in the set for words of length 3.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		/*
		 * For each length word found (i.e. 3 letter word, 4 letter word), will store a
		 * set of all words found with that length.
		 *
		 * Note that this only initializes the outer data structure! We cannot
		 * initialize the inner sets until we have keys to pair those sets with.
		 */
		HashMap<Integer, HashSet<String>> lengths = new HashMap<>();

		for (String word : text.toLowerCase().split(" ")) {
			// Determine length of word
			Integer length = word.length();

			// Check if we need to initialize the inner data structure
			// There are a couple ways to do this!
//			if (lengths.get(length) == null) {
//				HashSet<String> words = new HashSet<>();
//				words.add(word);
//
//				lengths.put(length, words);
//			}
//			else {
//				HashSet<String> words = lengths.get(length);
//				words.add(word);
//
//				lengths.put(length, words);
//			}

			// Again, we can eliminate the if block using the convenience methods.
			lengths.putIfAbsent(length, new HashSet<>());

			// Since we know the inner set is initialized by this point,
			// go ahead and add the word. Duplicates will be ignored!
			lengths.get(length).add(word);

			/*
			 * This can be confusing. We are getting the set stored WITHIN the map, and
			 * adding a word directly. We do not need to call put() again. This is related
			 * to mutability and references in Java which we will get into more detail
			 * later.
			 */
		}

		/*
		 * To iterate through a nested data structure, we need one loop per level of
		 * nesting. Here we have 2 levels, so we need 2 loops.
		 */
		for (Integer length : lengths.keySet()) {
			System.out.printf("%d", length);

			for (String word : lengths.get(length)) {
				System.out.printf(", %s", word);
			}

			System.out.printf("%n");
		}
	}

	/** Prevent instantiating this class of static methods. */
	private NestedMapDemo() {
	}
}
