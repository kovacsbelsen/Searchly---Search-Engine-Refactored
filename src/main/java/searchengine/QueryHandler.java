package searchengine;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This will be responsible for handling the users search query and output all
 * the relevant pages
 */
public class QueryHandler {

    final Charset CHARSET = StandardCharsets.UTF_8;
    private InvertedIndex invertedIndex;
    Ranker ranker;
    List<List<String>> wordcollection = new ArrayList<List<String>>();



    /**
     * 
     * @param iIndex index that contatins "web"-collection.
     */

     // Take InvertedIndex as Parameter
    public QueryHandler(InvertedIndex iIndex) throws IOException {
        this.invertedIndex = iIndex;
        this.ranker = new Ranker(iIndex);
    }


    /**
     * The method checks if the pages contain the keyword, which is equal to the
     * inputted word.
     * 
     * @param term a word from user input
     * @return returns a webset of pages containing the word
     */
    public WebSet lookUp(String term) {

        String input = term.toLowerCase();
        WebSet webSet = new WebSet();
        if (invertedIndex.hasTerm(input)) {
            for (String url : invertedIndex.getUrls(input)) {
                webSet.addPage(invertedIndex.getPage(url));
            }
        }
        return webSet;
    } 


    /**
     * Process the input from the webpage and calls the Search method, which
     * searches the webset for the keywords.
     * 
     * @param query the user input from the webpage
     * @return returns a byte[] for the webpage to visualise
     */
    public byte[] getMatchingWebpages(String query) {

        WebSet webcollection = WebSet.unify(getWebSets(query));

        List<Page> rankedPages = ranker.rankByIDF(webcollection, wordcollection);
        // remove comments below to use TF
        // List<Page> rankedPages = ranker.rankByTF(webcollection, wordcollection);
        // List<Page> rankedPages = ranker.rankByHashedIDF(webcollection, wordcollection);

        var response = new ArrayList<String>();
        for (Page page : rankedPages){
            response.add(page.toString());
        }

        byte[] responseByte = response.toString().getBytes(CHARSET);
        return responseByte;
    }


    /** 
     * Processing the input text, separating words into key segments if " or " is detected, creating subqueries if " " is detected, searches for keywords by utilizing the getPages method.
     * @param text take in a string to search for
     * @return returns a list of list of webset 
     * each webset corresponds to a single word
    */
    // note - searching for "'keyword' + or " and nothing else, reduces the result amount as opposed to searching only for the keyword
     
    public List<List<WebSet>> getWebSets(String text) {
        text = text.replace("%20", " ");
        text = text.toLowerCase();

        String[] expressions = text.split(" or ");
        List<String> words = Arrays.asList(expressions);
        List<List<WebSet>> websets = new ArrayList<>();
        String searchword;
        List<String> keywords = new ArrayList<>();
        List<List<String>> collect = new ArrayList<>();
        int n = expressions.length;

        for (int i = 0; i < n; i++) {

            websets.add(new ArrayList<WebSet>());
            String[] subquery = words.get(i).split(" ");
            int m = subquery.length;

            for (int j = 0; j < m; j++) {

                searchword = subquery[j];

                // array add elements from returned webset
                websets.get(i).add(lookUp(searchword));
            }

            keywords = Arrays.asList(subquery);
            collect.add(keywords);
        }

        wordcollection = new ArrayList<>(collect);
        
        return websets;
    }
}
