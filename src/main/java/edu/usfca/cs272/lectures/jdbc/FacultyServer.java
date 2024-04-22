package edu.usfca.cs272.lectures.jdbc;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Demonstrates how to interact with a database server using JDBC and Jetty.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
public class FacultyServer {
	/**
	 * Creates a database connection and Jetty server to allow users to interact
	 * with the faculty tables from the Relational Databases examples.
	 *
	 * @param args first argument is used as the database.properties file if present
	 * @throws Exception if unable to start server
	 */
	public static void main(String[] args) throws Exception {
		Path base = Path.of("src", "main", "resources");
		Path properties = base.resolve("database.properties");

		if (args.length > 0) {
			properties = base.resolve(args[0]);
		}

		// Attempt to connect to database and create servlet
		DatabaseConnector connector = new DatabaseConnector(properties);
		Set<String> expected = Set.of("faculty_names", "faculty_github", "faculty_courses");

		// Test database connection and setup
		try (Connection db = connector.getConnection()) {
			Set<String> tables = connector.getTables(db);
			System.out.println("Tables: " + tables);

			if (!tables.containsAll(expected)) {
				throw new SQLException("Missing required faculty tables in database.");
			}
		}

		// Default handler for favicon.ico requests
		ContextHandler defaultHandler = new ContextHandler("/favicon.ico");
		defaultHandler.setHandler(new DefaultHandler());

		// Main servlet handler
		FacultyServlet servlet = new FacultyServlet(connector);
		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(new ServletHolder(servlet), "/");

		HandlerList handlers = new HandlerList();
		handlers.addHandler(defaultHandler);
		handlers.addHandler(servletHandler);

		// Only setup server if we made it this far (otherwise an exception thrown)
		Server server = new Server(8080);
		server.setHandler(handlers);
		server.start();
		server.join();
	}
}
