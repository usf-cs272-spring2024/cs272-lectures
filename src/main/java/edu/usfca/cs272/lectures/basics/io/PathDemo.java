package edu.usfca.cs272.lectures.basics.io;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class demonstrates how to use the {@link Path} class to get basic path
 * information.
 *
 * @see java.nio.file.Path
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
public class PathDemo {
	/** Format string used for easy-to-read console output. */
	private static final String format = "%22s: %s%n";

	/**
	 * Outputs various path properties to the console.
	 *
	 * @param path to output path details
	 * @see java.nio.file.Path
	 */
	public static void pathDetails(Path path) {
		System.out.printf(format, "toString()", path.toString());
		System.out.printf(format, "isAbsolute()", path.isAbsolute());
		System.out.printf(format, "getParent()", path.getParent());
		System.out.printf(format, "getRoot()", path.getRoot());
		System.out.printf(format, "getFileName()", path.getFileName());
		System.out.printf(format, "normalize()", path.normalize());
		System.out.printf(format, "toAbsolutePath()", path.toAbsolutePath());
		System.out.printf(format, "toUri()", path.toUri());
		System.out.println();
	}

	/**
	 * Outputs various file properties to the console.
	 *
	 * @param path to output file details
	 * @throws IOException if an I/O error occurs
	 * @see java.nio.file.Files
	 */
	public static void fileDetails(Path path) throws IOException {
		System.out.printf(format, "toString()", path.toString());

		// Get basic information about the file.
		System.out.printf(format, "size()", Files.size(path));
		System.out.printf(format, "getOwner()", Files.getOwner(path));
		System.out.printf(format, "getLastModifiedTime()", Files.getLastModifiedTime(path));

		// Check whether the file is a directory or hidden.
		System.out.printf(format, "exists()", Files.exists(path));
		System.out.printf(format, "isDirectory()", Files.isDirectory(path));
		System.out.printf(format, "isHidden()", Files.isHidden(path));

		// Check the read/write/execute permissions of the file.
		System.out.printf(format, "isExecutable()", Files.isExecutable(path));
		System.out.printf(format, "isReadable()", Files.isReadable(path));
		System.out.printf(format, "isWritable()", Files.isWritable(path));
		System.out.println();
	}

	/**
	 * Demonstrates the {@link Path} class.
	 *
	 * @param args unused
	 * @throws IOException if an I/O error occurs
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * Relative paths are nice for users. It is much easier as a user to enter a
		 * relative path instead of an absolute path. However, these paths are not as
		 * useful for our programs. Notice below how much information is missing.
		 */
		Path relativeDirectory = Path.of("."); // try ".", "..", and "~"
		pathDetails(relativeDirectory);

		/*
		 * Also note that converting to an absolute path doesn't solve all the problems.
		 * There is a extra "." at the end of the path, so there are still some
		 * redundant and unnecessary elements in our path.
		 */
		Path absoluteDirectory = relativeDirectory.toAbsolutePath();
		pathDetails(absoluteDirectory);

		/*
		 * To convert the path into a format more useful for our programs, use the
		 * toAbsolutePath() and normalize() methods. This will convert the path to an
		 * absolute path, and then remove the redundant parts of the path. Now, notice
		 * how we are able to get more useful details about our path object.
		 */
		Path normalizedDirectory = absoluteDirectory.normalize();
		pathDetails(normalizedDirectory);

		/*
		 * You can also easily combine paths together without having to worry about
		 * different path separators on different systems (like "/" on Unix, Linux, or
		 * Mac systems and "\" on Windows systems).
		 */
		String slash = FileSystems.getDefault().getSeparator();

		Path srcDirectory = Path.of("src", "main", "java");
		pathDetails(srcDirectory);

		/*
		 * We have to do a little more work to get our source file from the
		 * package folder.
		 */
		String fileName = PathDemo.class.getSimpleName() + ".java";
		String packageName = PathDemo.class.getPackageName();
		Path packagePath = srcDirectory.resolve(packageName.replace(".", slash));

		Path fileRelativePath = packagePath.resolve(fileName);
		pathDetails(fileRelativePath);

		Path fileCanonicalPath = fileRelativePath.toAbsolutePath().normalize();
		pathDetails(fileCanonicalPath);

		/*
		 * Since Path objects contain hierarchy, we can also get different elements of
		 * that hierarchy.
		 */
		System.out.printf(format, "getRoot()", fileCanonicalPath.getRoot());
		System.out.printf(format, "getNameCount()", fileCanonicalPath.getNameCount());
		System.out.printf(format, "getName(0)", fileCanonicalPath.getName(0));
		System.out.printf(format, "subpath(1, 3)", fileCanonicalPath.subpath(1, 3));
		System.out.println();

		/*
		 * We can use the Files utility class to get information from the file system
		 * about the path in question.
		 */
		fileDetails(normalizedDirectory);
		fileDetails(fileCanonicalPath);
	}
}
