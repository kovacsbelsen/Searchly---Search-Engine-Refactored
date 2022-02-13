package searchengine;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class is responsible for creating a page with title, url and content
 */

public class Page {

    private String url;
    private String title;
    private List<String> contents;


    /**
     * Creates a page with a title, url and an empty list of contents
     * 
     * @param url - the url of the web page
     * @param title - the title of the web page
     */

    public Page(String url, String title) {
        this.url = url;
        this.title = title;
        contents = new ArrayList<>();
    }

    /**
     * Sets lines into a list of contents
     * 
     * @param currentContents of the page
     */
    //
    public void setContent(ArrayList<String> currentContents) {                
        contents = currentContents.stream().collect(Collectors.toList());
    }

    /**
     * Retrieves content of the page
     * 
     * @return list of contents
     */
    public List<String> getContent() {
        return contents;
    }

    /**
     * Retrieves title of the page
     * 
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves url of the page
     * 
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns size of the page
     * 
     * @return size of the page including the title
     */

    public int getContentSize() {
        return contents.size() + 1;
    }

    /**
     * Formats url and title fields based on the given JSON format
     */
    @Override
    public String toString() {
        return String.format("{\"url\": \"%s\", \"title\": \"%s\"}", url, title);
    }
    /**
     * This methods is created using VS code source code generation.
     * The aim is to make sure that two identical objects get the same hashcode.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contents == null) ? 0 : contents.hashCode());
        result = prime * result + getContentSize();
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
    
}
