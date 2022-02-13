package searchengine;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class is responsible for enabling search engine to support complex
 * queries.
 * It retrieves the intersection of pages that two or more search queries occur.
 */

public class WebSet {
    private Set<Page> webPages;

    /**
     * Constructor - creates a set of pages
     */
    public WebSet() {

        webPages = new HashSet<Page>();

    }

    /**
     * Responsible for retrieving all the pages that are in the set
     * 
     * @return all pages contained in the set
     */

    public Set<Page> getAll() {
        return webPages;
    }

    public int countPages(){
        int count = 0;
        for(Page page : webPages)count++; 
        return count;
    }

    /**
     * Responsible for creating a union between two sets
     * 
     * @param in - the input set
     * @return unified set that contains all pages from the set and the input set
     */

    public WebSet union(WebSet in) {

        WebSet newWebSet = new WebSet();

        for (Page page : webPages) {
            newWebSet.addPage(page);
        }

        for (Page page : in.getAll()) {
            newWebSet.addPage(page);
        }

        return newWebSet;
    }

    /**
     * Responsible for creating an intersection between two sets
     * 
     * @param in - the input set
     * @return a set that contains an intersected pages
     */
    public WebSet intersect(WebSet in) {

        WebSet newWebSet = new WebSet();
        Set<Page> inputPages = in.getAll();
        for (Page page : inputPages) {
            if (webPages.contains(page)) {
                newWebSet.addPage(page);
            }
        }

        return newWebSet;
    }

    /**
     * Responsible for adding pages into the set
     * 
     * @param page
     */

    public void addPage(Page page) {

        webPages.add(page);

    }

    /**
     * Responsible for unifying the intersections
     * 
     * @param webSetList - a list of intersected websets
     * @return unified list of intersections
     */

    public static WebSet unify(List<List<WebSet>> webSetList) {
        var unified = new WebSet();
        for (List<WebSet> forInter : webSetList) {
            if (forInter.size() > 0) {
                WebSet intersected = forInter.get(0);
                for (WebSet pages : forInter) {

                    intersected = intersected.intersect(pages);
                }
                unified = unified.union(intersected);
            }
        }
        return unified;
    }

    /**
     * These method are created using VS code source code generation
     * Responsible for making sure that two identical objects get the same hashcode
     */

    @Override
    public int hashCode() {
        return webPages.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

}
