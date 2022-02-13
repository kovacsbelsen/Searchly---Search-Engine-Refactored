package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)





public class RankerTest {


    private Ranker rankedTest;
    private InvertedIndex iIndex;
    private WebSet webSet;
    private Page page1;
    private Page page2;
    private Page page3;
    private Page page4;
    private Page page5;
    private Page page6;
    private List<List<String>> subQueries;


    @BeforeEach
    void prepareEmptySet() throws IOException {
        webSet = new WebSet();
        
        page1 = new Page("https://page1.com", "title1");
        String[] wordsForContent1 = {"word1", "word5", "word2"};
        page1.setContent(new ArrayList<String>(Arrays.asList(wordsForContent1)));
        webSet.addPage(page1);
        
        page2 = new Page("https://page2.com", "title2");
        String[] wordsForContent2 = {"word1"};
        page2.setContent(new ArrayList<String>(Arrays.asList(wordsForContent2)));
        webSet.addPage(page2);
        
        page3 = new Page("https://page3.com", "title3");
        String[] wordsForContent3 = {"word3", "word2", "word2"};
        page3.setContent(new ArrayList<String>(Arrays.asList(wordsForContent3)));
        webSet.addPage(page3);
        
        page4 = new Page("https://page4.com", "title4");
        String[] wordsForContent4 = {"word4"};
        page4.setContent(new ArrayList<String>(Arrays.asList(wordsForContent4)));
        webSet.addPage(page4);
        
        page5 = new Page("https://page5.com", "title5");
        String[] wordsForContent5 = {"word5", "word1", "word1", "word1", "word1"};
        page5.setContent(new ArrayList<String>(Arrays.asList(wordsForContent5)));
        webSet.addPage(page5);
        
        page6 = new Page("https://page6.com", "title6");
        String[] wordsForContent6 = {"word1", "word1", "word3", "word3", "word5"};
        page6.setContent(new ArrayList<String>(Arrays.asList(wordsForContent6)));
        webSet.addPage(page6);
        
        subQueries = new ArrayList<List<String>>();
        

        String filename = "data/test-file-ranker.txt";
        iIndex = new InvertedIndex(filename);
        rankedTest = new Ranker(iIndex);
    }
    @Test
    void rankByTF_tryingToSeeIfItWorks_returnsArrayList() {
        
        subQueries.add(Arrays.asList("word1"));
        assertNotNull(rankedTest.rankByTF(webSet, subQueries));
    }

    @Test
    void rankByTF_testingOneWord_page5IsBest(){
        subQueries.add(Arrays.asList("word1"));
        List<Page> rankedList = rankedTest.rankByTF(webSet, subQueries);
        assertEquals("https://page2.com", rankedList.get(0).getUrl());
    }

    @Test
    void rankByIDF_andOrWords_topCorrectOrder(){
        subQueries.add(Arrays.asList("word1", "word5"));
        List<Page> rankedList = rankedTest.rankByIDF(webSet, subQueries);
        assertEquals("https://page5.com", rankedList.get(0).getUrl());
        assertEquals("https://page2.com", rankedList.get(1).getUrl());
        assertEquals("https://page1.com", rankedList.get(2).getUrl());
        assertEquals("https://page6.com", rankedList.get(3).getUrl());
        assertEquals("https://page3.com", rankedList.get(4).getUrl());
        assertEquals("https://page4.com", rankedList.get(5).getUrl());
    }
    @Test
    void combinedTf_boxedArray_correct(){
        List<Double> tfs = Arrays.asList(0.1, 0.2, 0.0, 0.1, 0.0, 0.1);
        List<Integer> qIds = Arrays.asList(0, 0, 1, 1, 2, 2);
        assertEquals(0.3, rankedTest.combineTf(tfs, qIds), 1e-4);
    }
    @Test
    void combinedTf_jaggedArray_correct(){
        List<Double> tfs = Arrays.asList(0.1, 0.2, 0.0, 0.1, 0.0, 0.1);
        List<Integer> qIds = Arrays.asList(0, 0, 0, 0, 1, 1);
        assertEquals(0.4, rankedTest.combineTf(tfs, qIds), 1e-4);

    }

    @Test
    void rankByTF_andOrWords_correctOrder(){
        subQueries.add(Arrays.asList("word1", "word5"));
        subQueries.add(Arrays.asList("word3"));
        List<Page> rankedList = rankedTest.rankByTF(webSet, subQueries);
        assertEquals("https://page2.com", rankedList.get(0).getUrl());
        assertEquals("https://page5.com", rankedList.get(1).getUrl());
        assertEquals("https://page1.com", rankedList.get(2).getUrl());
        assertEquals("https://page6.com", rankedList.get(3).getUrl());
        assertEquals("https://page3.com", rankedList.get(4).getUrl());
        assertEquals("https://page4.com", rankedList.get(5).getUrl());
    }

}
