package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testNegativeInputs() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(-2);
    	heap.insert(-1);
    	heap.insert(-5);
    	assertEquals(3, heap.size());
    	assertTrue(!heap.isEmpty());
    	assertEquals(-5, heap.peekMin());
    	assertEquals(-5, heap.removeMin());
    	assertEquals(-2, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testManySameInput() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        heap.insert(3);
        heap.insert(3);
        heap.insert(3);
        assertEquals(4, heap.size());
        assertTrue(!heap.isEmpty());
        assertEquals(3, heap.peekMin());
    	assertEquals(3, heap.removeMin());
    	assertEquals(3, heap.peekMin());
    	assertEquals(3, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testEmptyHeap() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	assertEquals(0, heap.size());
    	try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // This is ok
        }
    	
    	try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // This is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testNullInsert() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	assertEquals(0, heap.size());
    	try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // This is ok
        }
    	
    	heap.insert(-2);
    	heap.insert(-1);
    	heap.insert(-5);
    	assertEquals(3, heap.size());
    	assertTrue(!heap.isEmpty());
    	
    	try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // This is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testResize() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	int cap = 100;
    	
    	for (int i = 0; i < cap; i++) {
    		heap.insert(i);
    		assertEquals(i + 1, heap.size());
    	}
    	
    	assertEquals(0, heap.peekMin());
    	assertEquals(0, heap.removeMin());
    	assertEquals(cap - 1, heap.size());
    	assertEquals(1, heap.peekMin());
    }
}
