
package searchengine;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class InvertedIndexTest {

    @Test
    void loadPages_fiveCorrectPages_FindFivePages() throws IOException{
        int expectedCorpusSize = 5;
        InvertedIndex invertedIndex = new InvertedIndex("data/test-file-invertedIndex.txt");
        int outputCorpusSize = invertedIndex.getCorpusSize();
        
        assertEquals(expectedCorpusSize, outputCorpusSize);
    }

    @Test
    void loadPages_twoOfFiveAreCorrect_FindsTwoPages() throws IOException {
        int expectedCorpusSize = 2;
        InvertedIndex invertedIndex = new InvertedIndex("data/test-file-errors.txt");
        int outputCorpusSize = invertedIndex.getCorpusSize();
        assertEquals(expectedCorpusSize, outputCorpusSize);
    }
    

    @ParameterizedTest
    @ValueSource(strings={"word1","word2","word3","word4","word5"})
    void hasTerm_matchesTitletoWord_correctlyFindsValues(String testTermsFromFile) throws IOException{
        InvertedIndex invertedIndex = new InvertedIndex("data/test-file-invertedIndex.txt");
        assertEquals(true, invertedIndex.hasTerm(testTermsFromFile));
    }


}

