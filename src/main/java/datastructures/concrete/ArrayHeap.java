package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
// import misc.exceptions.NotYetImplementedException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int heapSize;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
    	heapSize = 0;
    	heap = makeArrayOfT(NUM_CHILDREN * (NUM_CHILDREN + 1) + 1);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
    	if (heapSize == 0) {
    		throw new EmptyContainerException();
    	}
    	
    	T min = heap[0];
    	heap[0] = heap[heapSize - 1];
    	percolateDown(0);
    	heapSize--;
    	return min;
    }

    @Override
    public T peekMin() {
    	if (heapSize == 0) {
    		throw new EmptyContainerException();
    	}
    	
    	return this.heap[0];
    }

    @Override
    public void insert(T item) {
    	if (item == null) {
    		throw new IllegalArgumentException();
    	}
    	
    	if (isFull()) {
    		resize(heap.length * 2);
    	} 
    	
    	heap[heapSize] = item;
    	percolateUp(heapSize);
    	heapSize++;
    }

    @Override
    public int size() {
    	return this.heapSize;
    }
    
    private boolean isFull() {
    	return heapSize == heap.length;
    } 
    
    private void resize(int size) {
    	T[] temp = (T[]) (new Comparable[size]);
    	for (int i = 0; i < this.heapSize; i++) {
    		temp[i] = this.heap[i];
    	}
    	
    	this.heap = temp;
    }
    
    private void percolateUp(int index) {
    	T temp = heap[index];
    	while (index > 0 && temp.compareTo(heap[getParentIndex(index)]) < 0) {
    		heap[index] = heap[getParentIndex(index)];
    		index = getParentIndex(index);
    	}
    	heap[index] = temp;
    }
    
    private void percolateDown(int index) {
    	int childIndex;
    	T temp = heap[index];
    	while (getChildIndex(index, 1) < heapSize) {
    		childIndex = getMinIndex(index);
    		if (heap[childIndex].compareTo(temp) < 0) {
    			heap[index] = heap[childIndex];
    		} else {
    			break;
    		}
    		index = childIndex;
    	}
    	heap[index] = temp;
    }
    
    private int getParentIndex(int index) {
    	return (index - 1) / NUM_CHILDREN;
    }
    
    private int getChildIndex(int clusterIndex, int index) {
    	return NUM_CHILDREN * clusterIndex + index;
    }
    
    private int getMinIndex(int index) {
    	int currentIndex = getChildIndex(index, 1);
    	int k = 2;
    	int nextIndex = getChildIndex(index, k);
    	
    	while (k <= NUM_CHILDREN && nextIndex < heapSize) {
    		if (heap[nextIndex].compareTo(heap[currentIndex]) < 0) {
    			currentIndex = nextIndex;
    		}
    		k++;
    		nextIndex = getChildIndex(index, k);
    	}
    	
    	return currentIndex;
    }
}
