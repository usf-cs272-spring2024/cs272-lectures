package edu.usfca.cs272.templates.functional.lambdas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/lang/String.java#L2053
//https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/Comparator.java#L491-L495

public class StringSorter {
	// TODO Fix declaration
	class StringIncreasingLength implements Comparator<String> {
		@Override
		public int compare(String one, String two) {
			return Integer.compare(one.length(), two.length());
		}
	};

	public static void printList(String label, List<String> words) {
		System.out.printf("%9s: %s%n", label, words);
	}

	public static void main(String[] args) {
		List<String> words = new ArrayList<>();

		Collections.addAll(words, "ant", "AARDVARK", "Bee", "capybara", "deer",
				"elk", "elephant", "FOX", "gecko", "hippopatamus");

		printList("Original", words);

		Collections.shuffle(words);
		printList("Shuffled", words);
		
		Collections.sort(words);
		printList("Sorted", words);
		
		// TODO Fill in
	}
}
