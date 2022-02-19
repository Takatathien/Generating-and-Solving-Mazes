package datastructures.concrete;

import java.util.Random;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private IDictionary<Integer, Integer> bigFam;
    private int size;

    public ArrayDisjointSet() {
        size = 0;
        pointers = new int[2];
        bigFam = new ChainedHashDictionary<>();
       
    }

    @Override
    public void makeSet(T item) {
        int hashItem = item.hashCode();
        if (bigFam.containsKey(hashItem) && bigFam.get(hashItem).equals(item)) {
            throw new IllegalArgumentException();
        }
        bigFam.put(hashItem, size);
        if (size == pointers.length) {
            pointers = extendsArray(pointers);
        }
        pointers[size] = -1;
        size++;
    }

    @Override
    public int findSet(T item) {
        int hashItem = item.hashCode();
        if (!bigFam.containsKey(hashItem)) {
            throw new IllegalArgumentException();
        }
        int treeRoot = getPointer(bigFam.get(hashItem));
        updateRoot(bigFam.get(hashItem), treeRoot);
        return treeRoot;
    }

    @Override
    public void union(T oneItem, T secondItem) {
        int repOne = findSet(oneItem);
        int repTwo = findSet(secondItem);
        if (repOne == repTwo) {
            throw new IllegalArgumentException();
        }
        
        if (pointers[repOne] > pointers[repTwo]) {
        	pointers[repOne] = repTwo;
        } else if (pointers[repTwo] > pointers[repOne]) {
        	pointers[repTwo] = repOne;
        } else {
        	Random rand = new Random();
        	int ranNum = rand.nextInt(10);
        	if (ranNum % 2 == 0) {
        		pointers[repTwo] = repOne;
        		pointers[repOne]--;
        	} else {
        		pointers[repOne] = repTwo;
        		pointers[repTwo]--;
        	}
        }
    }
    
    private int getPointer(int index) {
        int pointer = pointers[index];
        if (pointer < 0) {
            return index;
        }
        return getPointer(pointer);
    }
    
    private void updateRoot(int child, int parent) {
    	if (child != parent) {
    		int prevParent = pointers[child];
            if (pointers[child] >= 0) {
            	pointers[child] = parent;
            }
            updateRoot(prevParent, parent);
    	}
    }
    
    private int[] extendsArray(int[] array) {
        int arrayLength = array.length;
        int[] arr = new int[2 * arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
}
