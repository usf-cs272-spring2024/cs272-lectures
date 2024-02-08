package edu.usfca.cs272.templates.basics.objects;

public class PrefixDemo {
	public static void main(String[] args) {
		String[] words = { "ant", "antelope", "ape", "bat", "badger", "cat", "catfish", "dog", "dragonfly" };
		PrefixMap map = new PrefixMap(2);

		try {
			System.out.println(map);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
