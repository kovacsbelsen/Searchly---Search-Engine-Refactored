
package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.IOException;
import java.net.BindException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class WebServerTest {
    WebServer server = null;

    @BeforeAll
    void setUp() {
        try {
            Random rnd = new Random();
            while (server == null) {
                try {
                    server = new WebServer(rnd.nextInt(60000) + 1024, "data/test-file.txt");
                } catch (BindException e) {
                    // port in use. Try again
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDown() {
        server.server.stop(0);
        server = null;
    }

    @Test
    void lookUpWebServer_multipleCases_findsWord1() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.server.getAddress().getPort());
        assertEquals(
                "[{\"url\": \"http://page1.com\", \"title\": \"title1\"}, {\"url\": \"http://page2.com\", \"title\": \"title2\"}]",
                httpGet(baseURL + "word1"));
    }

    @Test
    void lookUpWebServer_pageOneFound_findsWord2() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.server.getAddress().getPort());
        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}]",
                httpGet(baseURL + "word2"));
    }

    @Test
    void lookUpWebServer_pageTwoFound_findsWord3() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.server.getAddress().getPort());
        assertEquals("[{\"url\": \"http://page2.com\", \"title\": \"title2\"}]",
                httpGet(baseURL + "word3"));
    }

    @Test
    void lookUpWebServer_emptyPage_findsBlank() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.server.getAddress().getPort());
        assertEquals("[]",
                httpGet(baseURL + "word4"));
    }

    private String httpGet(String url) {
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            return client.send(request, BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "[]";
        }
    }

}
