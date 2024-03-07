package edu.usfca.cs272.lectures.debugging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class demonstrates basic logging and multithreading. This is example
 * includes multiple classes, indicated in the "See Also" section.
 *
 * @see CharacterFinder
 * @see CharacterCompare
 * @see CharacterBenchmark
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
public class CharacterCompare {
	/** Logger used for all output generated in this class. */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Compare two files, returning the difference in the number of characters using
	 * the {@link CharacterFinder#findCharacter(Path, char, boolean)} method.
	 *
	 * @param file1 first file to search
	 * @param file2 second file to search
	 * @param character character to look for in both files
	 * @return difference in characters
	 */
	public static int compareSequentially(Path file1, Path file2, char character) {
		log.debug("Comparing {} and {} sequentially.", file1, file2);

		int count1 = 0;
		int count2 = 0;

		try {
			count1 = CharacterFinder.findCharacter(file1, character, true);
			count2 = CharacterFinder.findCharacter(file2, character, true);
		}
		catch (IOException e) {
			log.catching(Level.DEBUG, e);
		}

		log.debug("Files {} and {} have a difference of {} '{}' characters.", file1, file2, count1 - count2, character);

		return count1 - count2;
	}

	/**
	 * A Runnable static nested class used to concurrently compare the number of
	 * characters in two files. Demonstrates one of two ways to create a
	 * {@link Thread} object.
	 */
	private static class CountThread extends Thread {
		/** The file to search */
		private final Path file;

		/** The character to look for */
		private final char character;

		/** The calculated character count */
		private int count;

		/**
		 * We cannot pass parameters to the {@link #run()} method, so we must pass them
		 * to the constructor here instead.
		 *
		 * @param file file to search
		 * @param character character to search for in file
		 */
		public CountThread(Path file, char character) {
			this.file = file;
			this.character = character;
			this.count = 0;
		}

		/**
		 * This method must match exactly, so we are unable to add parameters, change
		 * the return type, or throw any exceptions.
		 */
		@Override
		public void run() {
			try {
				count = CharacterFinder.findCharacter(file, character, true);
			}
			catch (IOException e) {
				// why do you think run() cannot throw exceptions?
				// because no guarantee parent thread is still around to catch it!
				log.catching(Level.DEBUG, e);
			}
		}
	}

	/**
	 * Compare two files, returning the difference in the number of characters using
	 * the {@link CharacterFinder#findCharacter(Path, char, boolean)} method.
	 *
	 * @param file1 first file to search
	 * @param file2 second file to search
	 * @param character character to look for in both files
	 * @return difference in characters
	 */
	public static int compareConcurrently(Path file1, Path file2, char character) {
		log.debug("Comparing {} and {} concurrently.", file1, file2);

		// create thread objects, threads will be in a "start" state
		CountThread counter1 = new CountThread(file1, character);
		CountThread counter2 = new CountThread(file2, character);

		// start the threads, placing them in a "runnable" state
		counter1.start();
		counter2.start();

		try {
			log.debug("Waiting for threads to complete.");

			// wait for the threads of execution to join back together again
			// try this code without the join statements!
			counter1.join();
			counter2.join();

			// once that happens, the worker threads are done
			log.debug("Threads finished.");
		}
		catch (InterruptedException e) {
			log.catching(Level.DEBUG, e);
		}

		String format = "Files {} and {} have a difference of {} '{}' characters.";
		log.debug(format, file1, file2, counter1.count - counter2.count, character);

		return counter1.count - counter2.count;
	}

	/**
	 * Demonstrates the {@link #compareSequentially} and
	 * {@link #compareConcurrently} methods.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		Path sherlock = Path.of("src", "main", "resources", "pg1661.txt");
		Path mobydick = Path.of("src", "main", "resources", "2701-0.txt");

		// make sure your environment setup is correct
		// you need to have a "text" folder with these files in it
		if (!Files.isReadable(sherlock) || !Files.isReadable(mobydick)) {
			log.error("Unable to read {} and {}.", sherlock, mobydick);
			return;
		}

		char character = 'c';

		int countSequentially = compareSequentially(sherlock, mobydick, character);
		int countConcurrently = compareConcurrently(sherlock, mobydick, character);

		assert countSequentially == countConcurrently;

		log.info("The file {} has {} {} '{}' characters than {} does.", sherlock.getFileName(), Math.abs(countSequentially),
				countSequentially > 0 ? "more" : "less", character, mobydick.getFileName());
	}

}
