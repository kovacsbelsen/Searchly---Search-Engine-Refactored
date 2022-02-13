package searchengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * This objects of this class contatins methods for ranking webpages in different ways
 */
public class Ranker {
    private InvertedIndex iIndex;

    private class RankedPage implements Comparable<RankedPage> {
        public Page page;
        public double score;

        RankedPage(Page page, double score) {
            this.score = score;
            this.page = page;
        }

        @Override
        public int compareTo(RankedPage other) {

            if (score == other.score) { // Should be handled better than relying on ulps.
                return page.getUrl().compareTo(other.page.getUrl());
            } else {
                return (int) Math.signum(other.score - this.score);
            }
        }

    }

    public Ranker(InvertedIndex iIndex) {
        this.iIndex = iIndex;
    }

    private ArrayList<Page> getSortedPages(ArrayList<RankedPage> pageList) {
        Collections.sort(pageList);
        var output = new ArrayList<Page>();
        for (RankedPage p : pageList) {
            output.add(p.page);
        }
        return output;
    }

    /**
     * Orders the given WebCollection with respect to the term frequencies
     * @param webCollectionSet the set of approved pages to be displayed
     * @param searchWordList a list containing inner lists that contain words from subqueries. 
     * @return an ordered list with most relevant page first.
     */
    public List<Page> rankByTF(WebSet webCollectionSet, List<List<String>> searchWordList) {
        return rankByTF(webCollectionSet, searchWordList, false);
    }

    /**
     * Orders the given WebCollection with respect to the inverted term frequencies 
     * @param webCollectionSet the set of approved pages to be displayed
     * @param searchWordList a list containing inner lists that contain words from subqueries. 
     * @return an ordered list with most relevant page first.
     */
    public List<Page> rankByIDF(WebSet webCollectionSet, List<List<String>> searchWordList) {
        return rankByTF(webCollectionSet, searchWordList, true);
    }

    private List<Page> rankByTF(WebSet webCollectionSet, List<List<String>> searchWordList, boolean useIdf) {
        var words = new ArrayList<String>();
        var qId = new ArrayList<Integer>();

        Integer nSub = 0;
        for (List<String> subQuery : searchWordList) {
            for (String s : subQuery)
                qId.add(nSub);
            words.addAll(subQuery);
            nSub++;
        }

        var rankedPages = new ArrayList<RankedPage>();
        // setting up results table
        for (Page approvedPage : webCollectionSet.getAll()) {
            List<Double> tfs = tfScores(approvedPage, words, useIdf);
            double pageScore = combineTf(tfs, qId);
            rankedPages.add(new RankedPage(approvedPage, pageScore));
        }

        return getSortedPages(rankedPages);
    }
    
    public double combineTf(List<Double> tfs, List<Integer> qId) {
        Double score = 0.0;
        double prevScore = 0.0;
        int prevId = qId.get(0);

        for (int i = 0; i < tfs.size(); i++) {
            boolean sameSubQuery = qId.get(i) == prevId;
            if (sameSubQuery) {
                score += tfs.get(i);
            } else {
                prevScore = Math.max(score, prevScore);
                prevId = qId.get(i);
                score = tfs.get(i);
            }
        }
        return Math.max(score, prevScore);
    }

    private List<Double> tfScores(Page page, List<String> words, boolean useIdf) {
        Double[] scores = new Double[words.size()];
        Arrays.fill(scores, 0.0);

        for (String word : page.getContent()) {
            if (words.contains(word)) {
                Integer index = words.indexOf(word);
                if (useIdf) {
                    scores[index] += 1.0 / page.getContent().size() * idf(word);
                } else {
                    scores[index] += 1.0 / page.getContent().size();
                }
            }
        }
        for (int i = words.size() - 1; i >= 0; i--) {
            int firstIndex = words.indexOf(words.get(i));
            boolean hasDuplicate = (i != firstIndex);
            if (hasDuplicate) {
                scores[i] = scores[firstIndex];
            }
        }

        List<Double> out = Arrays.asList(scores);
        return out;
    }

    private double idf(String term) {
        int nt = iIndex.getUrls(term).size();
        return Math.log10((double) iIndex.getCorpusSize() / nt);
    }

    private Map<String, Double> findTF(List<List<String>> SearchWordList, List<String> PageContent, int pageSize) {
        int counter = 0;

        Map<String, Double> findings = new HashMap<String, Double>();

        for (List<String> words : SearchWordList) { // all the sublists of searchwords
            for (String word : words) { // actual searchwords for each sublists
                for (String text : PageContent) { // for every word on the content of the given page
                    if (word.equals(text)) { // if searchword and word of contentpage are equal, continue
                        counter++; // increase word occurence counter
                    }
                }

                double tf = (double) counter / (double) pageSize; // calculate term frequency
                findings.put(word, tf); // add term frequency for the given word to a map
                counter = 0; // reset counter
            }
        }
        return findings;
    }

    private Map<String, Double> findIDF(List<List<String>> SearchWordList, int CorpusSize) {
        Map<String, Double> findings = new HashMap<String, Double>();

        for (List<String> words : SearchWordList) { // all the sublists of searchwords
            for (String word : words) { // actual searchwords for each sublists
                int PagesWithWord = iIndex.getUrls(word).size(); // find the number of pages containing the searchword

                double idf = Math.log10((double) CorpusSize / PagesWithWord); // run the IDF formula to calculate the
                                                                              // value of the word

                findings.put(word, idf);
            }
        }
        return findings;
    }

    // idf == log ( number of pages in the medium.txt / number of pages containing
    // search word, occurence irrelevant)
    // tf == searchword / amount of words on page

    public List<Page> rankByHashedIDF(WebSet WebCollectionSet, List<List<String>> SearchWordList) {

        int corpusSize = iIndex.getCorpusSize();
        Map<String, Double> wordoccurence = findIDF(SearchWordList, corpusSize); // for each word, get the idf of the
                                                                                 // word, add to map for key: word,
                                                                                 // value: idf

        // ------------------------------------------------------------------TERM
        // FREQUENCY----------------------------------------------

        var findingsonpage = new HashMap<Page, Map<String, Double>>(); // each page's individual result for finding the
                                                                       // keywords

        for (Page somepage : WebCollectionSet.getAll()) {
            List<String> content = somepage.getContent();
            int pageSize = content.size();

            Map<String, Double> term = findTF(SearchWordList, content, pageSize); // call findTF method, to search for
                                                                                  // all keywords within content,
                                                                                  // counting occurences
            findingsonpage.put(somepage, term); // add map of searchwords and their termfrequency to each page, THIS
                                                // VALUE IS NEEDED FOR IDF
        }
        // ------------------------------------------------------------------TERM
        // FREQUENCY----------------------------------------------

        // for each page in findingsonpage, get value hashmap, get TF value by
        // searchword and SOMEHOW multiply by the wordoccurence hashmap's value of the
        // same key searchword

        var output = new ArrayList<RankedPage>(); // this should be the map of the output, containing Pages and their
                                                  // proper IDF*TF values, either added together or .max()

        Map<Page, Map<String, Double>> processingmap = new HashMap<Page, Map<String, Double>>();

        Map<String, Double> processingwords = new HashMap<String, Double>();

        for (Page title : findingsonpage.keySet()) { // for each mapped page, get mapped words and values
            Map<String, Double> pageWordRank = findingsonpage.get(title);
            for (String tfsearchword : pageWordRank.keySet()) { // for each word, get the mapped value and add it to the
                                                                // term frequency
                double tfvalue = pageWordRank.get(tfsearchword);

                // new for loop to get the mapped value of the wordoccurence from the IDF
                // hashmap
                for (String idfsearchword : wordoccurence.keySet()) { // for each word, get the mapped value
                    if (tfsearchword.equals(idfsearchword)) { // if the tfsearchwors .equals idfsearchword , do the
                                                              // following : get the value of the key from the
                                                              // wordoccurence set and multiply it with the value of the
                                                              // tf
                        double idfvalue = wordoccurence.get(idfsearchword);
                        double result = idfvalue * tfvalue;
                        processingwords.put(idfsearchword, result); // add each word with their idf*tf result to this
                                                                    // submap
                        processingmap.put(title, processingwords); // add each submap to their pages
                        var page = new RankedPage(title, result);
                        if (!output.contains(page)) {
                            output.add(page);
                        }
                    }
                }
            }
        }
        return getSortedPages(output);
    }

}
