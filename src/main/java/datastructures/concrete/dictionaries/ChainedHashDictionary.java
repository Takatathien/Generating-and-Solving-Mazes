package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
// import misc.exceptions.NotYetImplementedException;

// import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int length;
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = makeArrayOfChains(10);
        this.length = chains.length;
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        IDictionary<K, V> element = chains[Math.abs(hash) % length];
        if (element == null) {
        	throw new NoSuchKeyException();
        }
        return element.get(key);
    }

    @Override
    public void put(K key, V value) {
        int hash = (key == null) ? 0 : key.hashCode();
        IDictionary<K, V> element = chains[Math.abs(hash) % length];
        if (element == null) {
        	element = new ArrayDictionary<K, V>();
        	element.put(key, value);
            size++;
            chains[Math.abs(hash) % length] = element;
        } else {
        	if (!element.containsKey(key)) {
        		size++;
        	}
        	element.put(key, value);
        	chains[Math.abs(hash) % length] = element;
        }
        
        if (size / length >= 1) {
        	int oldLength = length;
        	int newLength = getNextPrime(oldLength + oldLength);
        	IDictionary<K, V>[] oldChains = chains;
        	chains = (IDictionary<K, V>[]) new IDictionary[newLength];
        	length = chains.length;
        	size = 0;
        	for (int i = 0; i < oldLength; i++) {
        		IDictionary<K, V> temp = oldChains[i];
        		if (temp != null) {
        			for (KVPair<K, V> pair : temp) {
        				K k = pair.getKey();
        				V v = pair.getValue();
        				this.put(k, v);
        			}
        		}
        	}
        }
    }
    
    
    private int getNextPrime(int newLength) {
        if (newLength % 2 == 0) {
        	newLength++;
        }
        while (!isPrime(newLength)) {
            newLength += 2;
        }
        return newLength;
    }
   
    private boolean isPrime(int numb) {
        boolean found = false;
        int d = 2;
       
        while (!found && (d <= numb / 2))
        {
            found = numb % d == 0;
            d++;
        }
        return !found;
    }
    
    @Override
    public V remove(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        IDictionary<K, V> element = chains[Math.abs(hash) % length];
        if (element == null) {
        	throw new NoSuchKeyException();
        }
        if (element.containsKey(key)) {
        	size--; 
        } 
        return element.remove(key);   
    }

    @Override
    public boolean containsKey(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        IDictionary<K, V> element = chains[Math.abs(hash) % length];
        if (element == null) {
        	return false;
        }
        return element.containsKey(key);
    }

    @Override
    public int size() {
    	return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int length;
        private int bigIndex;
        IDictionary<K, V> temp;
        Iterator<KVPair<K, V>> itr;
        
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.length = chains.length;
            this.bigIndex = 0;
            
	        while (bigIndex < length && chains[bigIndex] == null) {
	        	bigIndex++;
	        }
	        
	        if (bigIndex < length) {
	        	temp = chains[bigIndex];
	        	itr = temp.iterator();
	        }
        }

        @Override
        public boolean hasNext() {   	
	        while (bigIndex < length && chains[bigIndex] == null) {
	        	bigIndex++;
	        }
	        
	        if (bigIndex < length) {
		        if (itr.hasNext()) {
		        	return true;
		        } else {
		        	bigIndex++;
			        while (bigIndex < length && chains[bigIndex] == null) {
			        	bigIndex++;
			        }
			        if (bigIndex >= length) {
			        	return false;
			        }
			        temp = chains[bigIndex];
		        	itr = temp.iterator();
			        return true;
		        }
	        } else {
	        	return false;
	        }
        }

        @Override
        public KVPair<K, V> next() {
            if (hasNext()) {
        		return itr.next();
            } else {
            	throw new NoSuchElementException();
            }
        }
    }
}

