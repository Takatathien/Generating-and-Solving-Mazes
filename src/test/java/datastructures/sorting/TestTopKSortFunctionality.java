package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import misc.exceptions.EmptyContainerException;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testSimpleUsage2() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 20; i > 0; i--) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(16 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testNegative() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        try {
        	IList<Integer> top = Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // This is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testSameInputs() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        
        list.add(20);
        list.add(20);
        list.add(20);
        list.add(20);
        list.add(20);
        
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(20, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testEmptyInputs() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	
    	IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(0, top.size());
        
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        
        IList<Integer> top2 = Searcher.topKSort(0, list);
        assertEquals(0, top2.size());
    }
}