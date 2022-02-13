package searchengine;

//import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WebSetTest {
    private WebSet webSet;
    private Page page1;
    private Page page2;
    private Page page3;
    private Page page4;
    private List<List<WebSet>> inListed = new ArrayList<List<WebSet>>();

    @BeforeEach
    void prepareEmptySet() {
        webSet = new WebSet();
        page1 = new Page("https://page1.com", "title1");
        page2 = new Page("https://page2.com", "title2");
        page3 = new Page("https://page3.com", "title3");
        page4 = new Page("https://page4.com", "title4");

    }

    @Test
    void getAll_noActionsApplied_emptySet() {
        var emptyPageSet = new HashSet<Page>();
        assertEquals(emptyPageSet, webSet.getAll());
    }

    @Test
    void equals_emptySets_equal() {
        assertEquals(webSet, new WebSet());
    }

    @Test
    void equals_twoPages_equal() {
        webSet.addPage(page1);
        webSet.addPage(page2);
        WebSet newSet = new WebSet();
        Page pageNew1 = new Page("https://page1.com", "title1");
        newSet.addPage(pageNew1);
        Page pageNew2 = new Page("https://page2.com", "title2");
        newSet.addPage(pageNew2);
        assertEquals(webSet, newSet);
    }

    @Test
    void equals_twoPages_unequal() {

        webSet.addPage(page1);
        webSet.addPage(page2);
        WebSet newSet = new WebSet();
        Page pageNew1 = new Page("https://page1.com", "title1");
        newSet.addPage(pageNew1);
        Page pageNew3 = new Page("https://page3.com", "title3");
        newSet.addPage(pageNew3);
        assertTrue(!webSet.equals(new WebSet()));
    }

    @Test
    void union_oneElementOnEmpty_oneElement() {
        var ws = new WebSet();
        ws.addPage(page1);
        var webSet2 = webSet.union(ws);
        assertEquals(ws, webSet2);
    }

    @Test
    void union_p123Up234_p1234() {
        var ws1 = new WebSet();
        ws1.addPage(page1);
        ws1.addPage(page2);
        ws1.addPage(page3);
        var ws2 = new WebSet();
        ws2.addPage(page2);
        ws2.addPage(page3);
        ws2.addPage(page4);

        WebSet res = ws2.union(ws1);
        WebSet full = new WebSet();
        full.addPage(page1);
        full.addPage(page2);
        full.addPage(page3);
        full.addPage(page4);
        assertEquals(full, res);
    }

    @Test
    void intersect_p123p234_p23() {
        var ws1 = new WebSet();
        ws1.addPage(page1);
        ws1.addPage(page2);
        ws1.addPage(page3);
        var ws2 = new WebSet();
        ws2.addPage(page2);
        ws2.addPage(page3);
        ws2.addPage(page4);

        WebSet res = ws2.intersect(ws1);
        WebSet intersect = new WebSet();
        intersect.addPage(page2);
        intersect.addPage(page3);
        assertEquals(intersect, res);
    }

    @Test
    void unify_emptySet_emptySet() {

        var innerList = new ArrayList<WebSet>();
        inListed.add(innerList);
        assertEquals(WebSet.unify(inListed), webSet);
    }

    @Test
    void unify_3unions_allPages() {
        var innerList1 = new ArrayList<WebSet>();
        var innerList2 = new ArrayList<WebSet>();
        var innerList3 = new ArrayList<WebSet>();
        WebSet ws123 = new WebSet();
        ws123.addPage(page1);
        ws123.addPage(page2);
        ws123.addPage(page3);
        WebSet ws234 = new WebSet();
        ws234.addPage(page2);
        ws234.addPage(page3);
        ws234.addPage(page4);
        WebSet ws12 = new WebSet();
        ws12.addPage(page1);
        ws12.addPage(page2);
        innerList1.add(ws123);
        innerList2.add(ws234);
        innerList3.add(ws12);
        inListed.add(innerList1);
        inListed.add(innerList2);
        inListed.add(innerList3);
        WebSet expectedSet = new WebSet();
        expectedSet.addPage(page1);
        expectedSet.addPage(page2);
        expectedSet.addPage(page3);
        expectedSet.addPage(page4);
        WebSet result = WebSet.unify(inListed);
        assertEquals(expectedSet, result);
    }

    @Test
    void unify_3unions2intersection_page234() {
        var innerList1 = new ArrayList<WebSet>();
        var innerList2 = new ArrayList<WebSet>();
        var innerList3 = new ArrayList<WebSet>();
        WebSet ws1234 = new WebSet();
        ws1234.addPage(page1);
        ws1234.addPage(page2);
        ws1234.addPage(page3);
        ws1234.addPage(page4);
        WebSet ws234 = new WebSet();
        ws234.addPage(page2);
        ws234.addPage(page3);
        ws234.addPage(page4);
        WebSet ws123 = new WebSet();
        ws123.addPage(page1);
        ws123.addPage(page2);
        ws123.addPage(page3);

        WebSet ws3 = new WebSet();
        ws3.addPage(page3);

        WebSet ws34 = new WebSet();
        ws34.addPage(page3);
        ws34.addPage(page4);

        WebSet ws234_2 = new WebSet();
        ws234_2.addPage(page2);
        ws234_2.addPage(page3);
        ws234_2.addPage(page4);

        innerList1.add(ws1234);
        innerList1.add(ws3);
        innerList2.add(ws234);
        innerList2.add(ws34);
        innerList3.add(ws123);
        innerList3.add(ws234_2);
        inListed.add(innerList1);
        inListed.add(innerList2);
        inListed.add(innerList3);
        WebSet expectedSet = new WebSet();
        expectedSet.addPage(page2);
        expectedSet.addPage(page3);
        expectedSet.addPage(page4);
        WebSet result = WebSet.unify(inListed);

        assertEquals(expectedSet, result);
    }

}
