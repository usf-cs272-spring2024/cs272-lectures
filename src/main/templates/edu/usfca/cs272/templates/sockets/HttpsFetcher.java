package edu.usfca.cs272.templates.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpsFetcher {

	public static Map<String, List<String>> fetch(URI uri) throws IOException {
		try (
				// TODO Fix socket initialization
				Socket socket = null;
				PrintWriter request = null;
				InputStreamReader input = null;
				BufferedReader response = null;
		) {
			// TODO Fix fetch implementation
			Map<String, List<String>> headers = null;
			return headers;
		}
	}

	public static Socket openConnection(URI uri) throws UnknownHostException, IOException {
		return null; // TODO Fix openConnection implementation
	}

	public static void printGetRequest(PrintWriter writer, URI uri) throws IOException {
		// TODO Fix printGetRequest implementation
	}

	public static Map<String, List<String>> processHttpHeaders(BufferedReader response) throws IOException {
		Map<String, List<String>> results = null; // TODO Fix processHttpHeaders implementation
		return results;
	}

	public static Map<String, List<String>> fetch(String url) throws IOException {
		return fetch(URI.create(url));
	}

	public static Map<String, List<String>> fetch(HttpClient client, URI uri) throws IOException {
		Map<String, List<String>> results = new HashMap<>();

		try {
			Thread.sleep(0); // TODO Fix fetch with HttpClient implementation
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		return results;
	}

	public static void main(String[] args) throws Exception {
		String[] urls = new String[] {
				"http://www.cs.usfca.edu/",
				"https://www.cs.usfca.edu/",
				"https://www.cs.usfca.edu/~cs272/",
				"https://www.cs.usfca.edu/~cs272/simple/double_extension.html.txt",
				"https://www.cs.usfca.edu/~cs272/nowhere.html"
		};

		HttpClient.Builder builder = HttpClient.newBuilder()
				.followRedirects(Redirect.NEVER);

		try (HttpClient client = builder.build();) {
			for (String url : urls) {
				URI uri = URI.create(url);
				System.out.println(uri);

//				var clientResults = fetch(client, uri);
//				System.out.println(clientResults);
//
//				var socketResults = fetch(uri);
//				System.out.println(socketResults);

				System.out.println();
			}
		}

//		URL url = URI.create(urls[0]).toURL();
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod("GET");
//		connection.setInstanceFollowRedirects(false);
//
//		System.out.println(url);
//		System.out.println(connection.getHeaderFields());
//		System.out.println();
//		connection.disconnect();
	}
}
