package edu.usfca.cs272.templates.debugging;

public class Debug {
	public static boolean on = false;

	public static void println(String message) {
		if (on) {
			System.out.println("[debug] " + message);
		}
	}
}
