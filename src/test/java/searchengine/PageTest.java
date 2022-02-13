package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class PageTest {

    @Test
    void allPublicMethods_input1_correctOutPut() {
        Page page = new Page("http://page1.com", "title1");
        String out = page.toString();
        String expected = "{\"url\": \"http://page1.com\", \"title\": \"title1\"}";
        assertEquals(expected, out);
    }

    @Test
    void allPublicMethods_input2_correctInput() {
        Page page = new Page("http://wikipedia.dk/p123", "Anatolia");
        String out = page.toString();
        String expected = "{\"url\": \"http://wikipedia.dk/p123\", \"title\": \"Anatolia\"}";
        assertEquals(expected, out);
    }

    @Test
    void allPublicMethods_noHTTP_correctOutPut() {
        Page page = new Page("page1", "title1");
        String out = page.toString();
        String expected = "{\"url\": \"page1\", \"title\": \"title1\"}";
        assertEquals(expected, out);
    }

    @Test
    void allPublicMethods_noTitle_correctOutPut() {
        Page page = new Page("http://page1.com", "");
        String out = page.toString();
        String expected = "{\"url\": \"http://page1.com\", \"title\": \"\"}";
        assertEquals(expected, out);
    }

    @Test
    void allPublicMethods_noURL_correctOutPut() {
        Page page = new Page("", "title1");
        String out = page.toString();
        String expected = "{\"url\": \"\", \"title\": \"title1\"}";
        assertEquals(expected, out);
    }

    @Test
    void allPublicMethods_noInput_correctOutPut() {
        Page page = new Page("", "");
        String out = page.toString();
        String expected = "{\"url\": \"\", \"title\": \"\"}";
        assertEquals(expected, out);

    }

    @Test
    void setContent_fullContentInput() {

        int expectedContentSize = 3;

        Page page = new Page("http://page1.com", "title1");
        ArrayList<String> contents = new ArrayList<>();
        String word1 = "word1";
        String word2 = "word2";
        String word3 = "word3";

        contents.add(word1);
        contents.add(word2);
        contents.add(word3);

        page.setContent(contents);
        int outputContentSize = page.getContentSize() - 1;

        assertEquals(expectedContentSize, outputContentSize);

    }

    @Test
    void setContent_noContent() {

        int expectedContentSize = 0;

        Page page = new Page("http://page1.com", "title1");
        ArrayList<String> contents = new ArrayList<>();
    
        page.setContent(contents);
        int outputContentSize = page.getContentSize() - 1;

        assertEquals(expectedContentSize, outputContentSize);

    }

}
