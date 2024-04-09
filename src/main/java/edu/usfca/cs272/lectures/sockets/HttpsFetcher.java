package edu.usfca.cs272.lectures.sockets;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 * Demonstrates how to use the built-in {@link HttpClient} to fetch the headers
 * and content from a URI on the web, as well as how to setup the {@link Socket}
 * connections to do the same manually (well suited for web crawling).
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
public class HttpsFetcher {
	/**
	 * Fetches the headers and content for the specified URI. The content is placed
	 * as a list of all the lines fetched under the "Content" key.
	 *
	 * @param uri the URI to fetch
	 * @return a map with the headers and content
	 * @throws IOException if unable to fetch headers and content
	 *
	 * @see #openConnection(URI)
	 * @see #printGetRequest(PrintWriter, URI)
	 * @see #processHttpHeaders(BufferedReader)
	 */
	public static Map<String, List<String>> fetch(URI uri) throws IOException {
		try (
				Socket socket = openConnection(uri);
				PrintWriter request = new PrintWriter(socket.getOutputStream());
				InputStreamReader input = new InputStreamReader(socket.getInputStream(), UTF_8);
				BufferedReader response = new BufferedReader(input);
		) {
			// make HTTP GET request of the web server
			printGetRequest(request, uri);

			// the headers will be first in the response
			Map<String, List<String>> headers = processHttpHeaders(response);

			// read everything remaining in socket reader as the content
			List<String> content = response.lines().toList();
			headers.put("content", content);

			return headers;
		}
	}

	/**
	 * Uses a {@link Socket} to open a connection to the web server associated with
	 * the provided URI. Supports HTTP and HTTPS connections.
	 *
	 * @param uri the URI to connect
	 * @return a socket connection for that URI
	 * @throws UnknownHostException if the host is not known
	 * @throws IOException if an I/O error occurs when creating the socket
	 *
	 * @see URL#openConnection()
	 * @see URLConnection
	 * @see HttpURLConnection
	 */
	public static Socket openConnection(URI uri) throws UnknownHostException, IOException {
		String protocol = uri.getScheme();
		String host = uri.getHost();

		boolean https = protocol != null && protocol.equalsIgnoreCase("https");
		int defaultPort = https ? 443 : 80;
		int port = uri.getPort() < 0 ? defaultPort : uri.getPort();

		SocketFactory factory = https ? SSLSocketFactory.getDefault() : SocketFactory.getDefault();
		return factory.createSocket(host, port);
	}

	/**
	 * Writes a simple HTTP v1.1 GET request to the provided socket writer.
	 *
	 * @param writer a writer created from a socket connection
	 * @param uri the URI to fetch via the socket connection
	 * @throws IOException if unable to write request to socket
	 *
	 * @see Builder#GET()
	 */
	public static void printGetRequest(PrintWriter writer, URI uri) throws IOException {
		String host = uri.getHost();
		String resource = Objects.requireNonNullElse(uri.getPath(), "/");

		writer.printf("GET %s HTTP/1.1\r\n", resource);
		writer.printf("Host: %s\r\n", host);
		writer.printf("Connection: close\r\n");
		writer.printf("\r\n");
		writer.flush();
	}

	/**
	 * Gets the header fields from a reader associated with a socket connection.
	 * Requires that the socket reader has not yet been used, otherwise this method
	 * will return unpredictable results.
	 *
	 * @param response a reader created from a socket connection
	 * @return a map of header fields to a list of header values
	 * @throws IOException if unable to read from socket
	 *
	 * @see URLConnection#getHeaderFields()
	 * @see HttpHeaders#map()
	 */
	public static Map<String, List<String>> processHttpHeaders(BufferedReader response) throws IOException {
		Map<String, List<String>> results = new HashMap<>();

		// first line will be the http status line (status code and description)
		// null used by early Java versions (see
		String line = response.readLine();
		results.put(null, List.of(line));

		// remaining lines until first blank line are the other headers
		while ((line = response.readLine()) != null && !line.isBlank()) {
			String[] split = line.split(":\\s+", 2);
			assert split.length == 2;

			split[0] = split[0].toLowerCase();
			results.computeIfAbsent(split[0], x -> new ArrayList<>()).add(split[1]);
		}

		return results;
	}

	/**
	 * See {@link #fetch(URI)} for details.
	 *
	 * @param uri the URI to fetch
	 * @return a map with the headers and content
	 * @throws URISyntaxException if unable to convert String to URI
	 * @throws IOException if unable to fetch headers and content
	 *
	 * @see #fetch(URI)
	 */
	public static Map<String, List<String>> fetch(String uri) throws URISyntaxException, IOException {
		return fetch(new URI(uri));
	}

	/**
	 * Fetches the headers and content for the specified URI using the built-in Java
	 * {@link HttpClient}. The content is placed as a list of all the lines fetched
	 * under the "content" key.
	 *
	 * @param client - the HTTP client to use
	 * @param uri - the URI to fetch
	 *
	 * @return a map of headers and content
	 * @throws IOException if unable to fetch headers and content
	 */
	public static Map<String, List<String>> fetch(HttpClient client, URI uri) throws IOException {
		// create placeholder for results
		Map<String, List<String>> results = new HashMap<>();

		// create GET request
		HttpRequest request = HttpRequest.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.uri(uri)
				.GET()
				.build();

		// convert response body to stream of lines
		BodyHandler<Stream<String>> handler = BodyHandlers.ofLines();

		try {
			// synchronously get response
			HttpResponse<Stream<String>> response = client.send(request, handler);

			// add content, status code, and headers
			results.put("content", response.body().toList());
			results.put(null, List.of(Integer.toString(response.statusCode())));
			results.putAll(response.headers().map());
		}
		catch (InterruptedException e) {
			// HttpClient typically manages its own thread and connection pool
			// just like our threaded code, it can get interrupted!
			Thread.currentThread().interrupt();
		}

		return results;
	}

	/**
	 * Demonstrates the {@link #fetch(URI)} method.
	 *
	 * @param args unused
	 * @throws Exception if unable to fetch url
	 */
	public static void main(String[] args) throws Exception {
		String[] urls = new String[] {
				"http://www.cs.usfca.edu/", // 302 -> https
				"https://www.cs.usfca.edu/", // 302 -> myusf
				"https://www.cs.usfca.edu/~cs272/", // 200
				"https://www.cs.usfca.edu/~cs272/simple/double_extension.html.txt", // text/plain
				"https://www.cs.usfca.edu/~cs272/nowhere.html" // 404
		};

		// create builder that does not follow redirects
		HttpClient.Builder builder = HttpClient.newBuilder()
				.followRedirects(Redirect.NEVER);

		// create client in try-with-resources to make sure gets closed
		try (HttpClient client = builder.build();) {
			for (String url : urls) {
				URI uri = URI.create(url);
				System.out.println(uri);

				var clientResults = fetch(client, uri);
				System.out.println(clientResults);

				var socketResults = fetch(uri);
				System.out.println(socketResults);

				System.out.println();
			}
		}

		/*
		 * The approaches differ in the following ways:
		 *
		 * 1) The HttpClient only returns the response status code (the 3 digit
		 * number, e.g. 404), but the socket returns the status line (including HTTP
		 * version and reason text, e.g. HTTP/1.1 404 NOT FOUND). The HttpClient
		 * supports HTTP/2, which only uses the code (without the reason text).
		 *
		 * 2) The HttpClient returns header field names in all lowercase. HTTP headers
		 * are compared case-insensitive, but HTTP/2 requires the field names be
		 * converted to lowercase prior to encoding.
		 *
		 * 3) The HttpClient does not use the "Connection" header, which is not used in
		 * HTTP/2. The socket version does, however, so that the sockets close faster.
		 * (Alternatively, it can track when the "content-length" has been read, then
		 * close the connection.) The HttpClient also uses a connection pool and can be
		 * reused, whereas the socket connections are not being reused.
		 *
		 * 4) The Socket version is easier to customize for efficient web crawling using
		 * an existing thread pool and work queue.
		 */

		// older version of getting headers and content (for reference)
		// note the null key for the response status and case of header field names

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
