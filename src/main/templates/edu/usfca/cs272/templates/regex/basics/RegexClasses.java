package edu.usfca.cs272.templates.regex.basics;

import edu.usfca.cs272.templates.regex.RegexHelper;

public class RegexClasses extends RegexHelper {
	public static void main(String[] args) {
		// Sally Sue sells 76 sea-shells, by   the sea_shore.
		console.println(RegexHelper.sample);
		console.println();

		showMatches(sample, "s");
		console.println();
	}
}
