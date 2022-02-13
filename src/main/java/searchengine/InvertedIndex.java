package searchengine;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * This datstructure hold a representiation of all pages, which cna be accesed by a searchterm.
 */
public class InvertedIndex {
    private Map<String, Set<String>> indexedPages; 
    private Map<String, String> titles; // might be redundant
    public int corpusSize = 0;
    private Map<String, Page> allPages;

    /**
     * Initializes the inverted index by setting th fields and calling loadPages()
     * @param filename of the file containing the "web-collection"
     * @throws IOException
     */
    public InvertedIndex(String filename) throws IOException{
        indexedPages = new HashMap<String, Set<String>>();
        titles = new HashMap<String, String>();
        allPages = new HashMap<String, Page>();
        loadPages(filename);
    }


    /**
     * Looks up in the Set of url and title
     * @param url String
     * @return title of page with corresponding url
     */
    public String getTitle(String url){
        return titles.get(url);
    }

    /**
     * 
     * @param url
     * @return
     */
    public Page getPage(String url){
        return allPages.get(url);
    }

    /**
     * 
     * @param term
     * @return
     */
    public Set<String> getUrls(String term){
        return indexedPages.get(term);
    }

    /**
     * 
     * @param term is the key 
     * @return 
     */
    public boolean hasTerm(String term){
        return indexedPages.containsKey(term);
    }


    /**
     * The method seperates the file into an inverted index, a page book, and title
     * @param filename - name of the file that the data comes from
     * @throws IOException
     */
    void loadPages(String filename) throws IOException {
        indexedPages = new HashMap<String, Set<String>>();
        titles = new HashMap<String, String>();
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(filename));
            String currentUrl = "";
            String currentTitle = "";
            ArrayList<String> currentContents = new ArrayList<String>();
            int urlIndex = 0;
            int i = 0;
            while(i < lines.size()){
                String term = lines.get(i).toLowerCase();

                if (term.startsWith("*page")) {
                    if (!currentUrl.isEmpty() && !currentContents.isEmpty() && !currentTitle.isEmpty()) {
                        pageCreator(currentUrl, currentTitle, currentContents);
                        currentContents = new ArrayList<String>();
                    };
                    currentUrl = term.substring(6);
                    urlIndex = i;

                } else if (i == urlIndex + 1) {
                    currentTitle = term;
                    titles.put(currentUrl, currentTitle);
                } else {
                    insert(term, currentUrl);
                    currentContents.add(term);
                }
                i++;
            }

            if (!currentUrl.isEmpty() && !currentContents.isEmpty() && !currentTitle.isEmpty()) {
                pageCreator(currentUrl, currentTitle, currentContents);
                currentContents = new ArrayList<String>();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    

    // 
    /**
     * This should return the amount of pages in the chosen datafile
     * @return the size of the corpus
     */
    public int getCorpusSize(){
        return corpusSize;
    }

    private void pageCreator(String currentUrl, String currentTitle, ArrayList<String> currentContents){
        Page newPage = new Page(currentUrl, currentTitle);
        newPage.setContent(currentContents);
        allPages.put(currentUrl, newPage);
        corpusSize += 1;
    }

    private void insert(String word, String url) {
        if (!indexedPages.containsKey(word)) indexedPages.put(word, new HashSet<String>());
        indexedPages.get(word).add(url);
    }
}