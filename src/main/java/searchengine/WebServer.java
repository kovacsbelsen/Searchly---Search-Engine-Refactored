package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


/**
 * This server encapsulates the server used for our Searchly page This is the
 * main entry point of the project.
 */
public class WebServer {
  static final int PORT = 8080;
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;
  QueryHandler queryHandler;
  InvertedIndex iIndex;
  HttpServer server;
  

  /**
   * The constructor reads in "Web"-collection files to the pages, creates the
   * webserver and pushes content to it
   * 
   * @param port     Port that server uses default i 8080
   * @param filename file that contains the name of "web"-collection. Usually
   *                 config.txt
   * @throws IOException if the HttpExchange server cannot be created
   */
  public WebServer(int port, String filename) throws IOException {
    server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
    createAllContext();
    server.start();
    displayLocalhost(port);
    iIndex = new InvertedIndex(filename);
    queryHandler = new QueryHandler(iIndex);
  }

  /**
   * Creates all content needed that defines the initial server
   */
  private void createAllContext() {
    server.createContext("/", io -> respond(io, 200, "text/html", getFile("web/index.html")));
    server.createContext("/search", io -> search(io));

    // Trying to change our Favicon
    server.createContext("/favicon.ico", io -> respond(io, 200, "image/x-icon", getFile("web/favicon.ico")));

    // ADDDING LOGO MN
    server.createContext("/FullSearchly.jpg", io -> respond(io, 200, "image/jpg", getFile("web/FullSearchly.jpg")));

    server.createContext("/code.js", io -> respond(io, 200, "application/javascript", getFile("web/code.js")));
    server.createContext("/style.css", io -> respond(io, 200, "text/css", getFile("web/style.css")));
  }

  /**
   * Display the terminal message with link to the webserver
   * 
   * @param port - the port created in the WebServer Constructor
   */
  private void displayLocalhost(int port) {
    String msg = " WebServer running on http://localhost:" + port + " ";
    System.out.println("╭" + "─".repeat(msg.length()) + "╮");
    System.out.println("│" + msg + "│");
    System.out.println("╰" + "─".repeat(msg.length()) + "╯");
  }

  /**
   * This will use the search term from the user and respond to the user will be
   * json object with the relevant pages.
   * 
   * @param io Object representation of the HTTP response
   */
  void search(HttpExchange io) {
    String searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
    byte[] outputPages = queryHandler.getMatchingWebpages(searchTerm);
    respond(io, 200, "application/json", outputPages);
    
  }

  /**
   * 
   * @param filename path to file from the root
   * @return a byte array version of the filename to avoid special character
   *         errors, returns empty byte array if it doesn't succeed
   */
  byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }

  /**
   * 
   * @param io       Object representation of the HTTP response
   * @param code     Status code - 200 if it is successful
   * @param mime     determines how to process the URL
   * @param response JSON objects of matching pages
   */
  void respond(HttpExchange io, int code, String mime, byte[] response) {
    try {
      // Example: .format example: text/plain;charset=UTF-8
      io.getResponseHeaders().set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
      io.sendResponseHeaders(200, response.length);
      io.getResponseBody().write(response);
    } catch (Exception e) {
    } finally {
      io.close();
    }
  }

  public static void main(final String... args) throws IOException {
    String filename = Files.readString(Paths.get("config.txt")).strip();
    new WebServer(PORT, filename);
  }
}
