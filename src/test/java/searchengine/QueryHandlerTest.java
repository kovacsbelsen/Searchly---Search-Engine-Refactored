
package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class QueryHandlerTest {

    private QueryHandler queryTest;
    private Page pageTest;
    private InvertedIndex iIndex;

    @Test
    void lookUp_addsToWebSet_isAddedCorrectly() throws IOException {
        WebSet webTest = new WebSet();
        String url = "http://page1.com";
        String title = "title1";
        ArrayList<String> content = new ArrayList<String>();
        String term = "word1";
        content.add(term);
        pageTest = new Page(url, title);
        pageTest.setContent(content);
        webTest.addPage(pageTest);
        String filename = "data/test-file-queryhandler.txt";
        InvertedIndex iIndex = new InvertedIndex(filename);
        QueryHandler queryHandler = new QueryHandler(iIndex);
        WebSet checkItOut = queryHandler.lookUp(term);
        assertEquals(webTest.getAll(), checkItOut.getAll());
    }

    @Test
    void lookUp_searchTermFindsMatches_termsAreAddedToSet() throws IOException {
        String filename = "data/enwiki-medium.txt";
        queryTest = new QueryHandler(new InvertedIndex(filename));
        String searchTerm = "hey";
        assertNotNull(queryTest.lookUp(searchTerm));
    }

    @Test
    void lookUp_searchTermFindsMatches_twoTermsAreFound() throws IOException {
        String filename = "data/enwiki-medium.txt";
        queryTest = new QueryHandler(new InvertedIndex(filename));
        String searchTerm = "hey";
        assertEquals(2, queryTest.lookUp(searchTerm).countPages());
    }

    @Test
    void getMatchingWebpages_searchTermFindsNoMatches_countIsZero() throws IOException {
        String filename = "data/enwiki-medium.txt";
        queryTest = new QueryHandler(new InvertedIndex(filename));
        String searchTerm = "hey";
        assertNotNull(queryTest.getMatchingWebpages(searchTerm));
    }

    @Test
    void getWebSets_searchTermFindsMatches_WebSetIsReturned() throws IOException {
        String filename = "data/enwiki-medium.txt";
        queryTest = new QueryHandler(new InvertedIndex(filename));
        String searchTerm = "hey";
        assertNotNull(queryTest.getMatchingWebpages(searchTerm));
    }

}
