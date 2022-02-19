package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
	protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
	
	@Test(timeout=10*SECOND)
    public void testAddAndRemoveMinMany() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	int cap = 1000000;
    	
    	for (int i = cap - 1; i <= 0 ; i--) {
    		heap.insert(i);
    		assertEquals(i + 1, heap.size());
    		assertEquals(i, heap.peekMin());
    		assertEquals(i, heap.removeMin());
    		assertEquals(i + 1, heap.peekMin());
    	}
    }
	
	@Test(timeout=10*SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap1 = 1000000;
        int cap2 = 50000;
        for (int i = 0; i < cap1; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(cap2, list);
        assertEquals(cap2, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(cap1 - cap2 + i, top.get(i));
        }
    }
}
