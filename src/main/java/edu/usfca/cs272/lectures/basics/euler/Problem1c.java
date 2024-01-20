package edu.usfca.cs272.lectures.basics.euler;

import java.util.Scanner;

/**
 * Project Euler Problem 1 is stated as follows:
 *
 * <blockquote> If we list all the natural numbers below 10 that are multiples
 * of 3 or 5, we get 3, 5, 6 and 9. The sum of these multiples is 23. Find the
 * sum of all the multiples of 3 or 5 below 1000. </blockquote>
 *
 * This example illustrates a more reusable and generalized approach with
 * additional code to handle flexible user input.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
public class Problem1c {
	/**
	 * Prompts the user for a valid non-negative integer until one is provided.
	 *
	 * @return non-negative integer value
	 */
	public static int inputNonNegativeInteger() {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Enter a non-negative integer: ");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				try {
					int value = Integer.parseInt(line);

					if (value < 0) {
						System.out.print("Enter a NON-NEGATIVE integer: ");
					}
					else {
						return value;
					}
				}
				catch (NumberFormatException e) {
					System.out.print("Enter a non-negative INTEGER: ");
				}
			}
		}

		return 0;
	}

	/**
	 * Prints the sum of multiples of 3 or 5 less than a user-specified maximum
	 * value to the console. The maximum value may be specified via a parameter or
	 * via a prompt.
	 *
	 * @param args if provided specifies the maximum value
	 */
	public static void main(String[] args) {
		int max;

		// try to parse the first argument
		try {
			max = Integer.parseInt(args[0]);

			if (max < 0) {
				throw new NumberFormatException("Integer value must be non-negative.");
			}
		}
		// if that fails, prompt the user for a non-negative integer
		catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			max = inputNonNegativeInteger();
		}

		int result = Problem1b.sumMultiples(max);
		String format = "The sum of multiples of 3 or 5 less than %d is %d.";
		System.out.printf(format, max, result);
	}

	/*
	 * Our main() method is now more generalized and reusable, but look at how much
	 * code we had to create to deal with user input. Since you cannot trust user
	 * input, expect to have to add quite a bit of code to validate the input before
	 * using it!
	 */

	/** Creates a new instance of this class. */
	public Problem1c() {}
}
