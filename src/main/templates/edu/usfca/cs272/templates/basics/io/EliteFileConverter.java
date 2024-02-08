package edu.usfca.cs272.templates.basics.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EliteFileConverter {
	public static char toLeet(char letter) {
		boolean random = Math.random() < 0.5;

		// https://docs.oracle.com/en/java/javase/17/language/switch-expressions.html
		return switch (letter) {
			case 'a', 'A' -> '\0'; // TODO Implement this case
			case 'b', 'B' -> '8';
			case 'e', 'E' -> '3';
			case 'i', 'I' -> '!';
			case 'l', 'L' -> '1';
			case 'o', 'O' -> '0';
			case 's', 'S' -> random ? '5' : '$';
			case 't', 'T' -> '7';
			default -> '\0'; // TODO Implement this case
		};
	}

	public static String toLeet(String text, double threshold) {
		// TODO Implement toLeet(String, double) method
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	public static String toLeet(String text) {
		// TODO Implement toLeet(String) method
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	public static void toLeetStrings(Path input, Path output) throws IOException {
		// TODO Implement toLeetSpeakMemoryIntensive method
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	public static void toLeetLists(Path input, Path output) throws IOException {
		// TODO Implement toLeetSpeakMemoryIntensive method
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	public static void toLeetBuffered(Path input, Path output) throws IOException {
		// TODO Implement toLeetSpeak(Path, Path) method
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	public static void main(String[] args) throws Exception {
		// https://docs.oracle.com/en/java/javase/17/language/text-blocks.html
		String text = ""; // TODO Fill in value

		System.out.println(text);
//		System.out.println(toLeet(text));
//		System.out.println(toLeet(text, 0.25));
//		System.out.println(toLeet(text, 1.00));

		String typeName = EliteFileConverter.class.getTypeName();
		String typePath = typeName.replace('.', File.separatorChar) + ".java";

		Path base = Path.of("src", "main", "templates");
		Path input = base.resolve(typePath);
		System.out.println(Files.isReadable(input));

//		Path output = Path.of("out", EliteFileConverter.class.getSimpleName() + ".txt");
//		Files.createDirectories(output.getParent());
//
//		toLeetStrings(input, output);
//		toLeetLists(input, output);
//		toLeetBuffered(input, output);
//
//		Path nowhere = Path.of("nowhere");
//		toLeetBuffered(nowhere, nowhere);
	}
}
