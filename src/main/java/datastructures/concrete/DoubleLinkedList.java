package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
    	Node<T> input = new Node<T>(item);
    	if (this.size() == 0) {
    		front = input;
    		back = input;
    		size++;
    	} else {
    		Node<T> temp = back;
    		back.next = input;
    		back = input;
    		input.prev = temp;
    		size++;
    	}
    }

    @Override
    public T remove() {
    	if (this.size == 0) {
    		throw new EmptyContainerException();
    	} else if (this.size() == 1) {
    		Node<T> temp = new Node<T>(back.data);
    		front = null;
    		back = null;
    		size--;
    		return temp.data;
    	} else {
    		T temp = back.data;
    		Node<T> current = back.prev;
    		current.next = null;
    		back = current;
    		size--;
    		return temp;
    	}
    }

    @Override
    public T get(int index) {
    	if (index < 0 || index >= this.size()) {
    		throw new IndexOutOfBoundsException();
    	} else {
    		Node<T> current = front;
        	int i = 0;		
        	while (i < index) {
        		current = current.next;
        		i++;
        	}
        	return current.data;
    	}
    }

    @Override
    public void set(int index, T item) {
    	if (index < 0 || index >= this.size()) {
    		throw new IndexOutOfBoundsException();
    	} else if (this.size() == 1) {
    		Node<T> input = new Node<T>(item);
    		front = input;
    		back = input;
    	} else if (index == 0) {
    		Node<T> temp = front;
    		Node<T> current = temp.next;
    		Node<T> input = new Node<T>(item);
    		current.prev = input;
    		input.next = current;
    		front = input;
    		temp.prev = null;
    		temp.next = null;
    	} else if (index == this.size() - 1) {
    		Node<T> temp = back;
    		Node<T> current = back.prev;
    		Node<T> input = new Node<T>(item);
    		current.next = input;
    		input.prev = current;
    		back = input;
    		temp.prev = null;
    		temp.next = null;
    	} else {
    		Node<T> current = front;
    		Node<T> input = new Node<T>(item);
    		int i = 0;
    		while (i < index) {
    			current = current.next;
    			i++;
    		}
    		Node<T> tempPrev = current.prev;
    		Node<T> tempNext = current.next;
    		tempPrev.next = input;
    		input.prev = tempPrev;
    		tempNext.prev = input;
    		input.next = tempNext;
    		current.prev = null;
    		current.next = null;
    	}
    }

    @Override
    public void insert(int index, T item) {
    	if (index < 0 || index >= this.size() + 1) {
    		throw new IndexOutOfBoundsException();
    	} else if (index == 0 && this.size() == 0) {
    		this.add(item);
    	} else if (index == 0) {
    		Node<T> current = front;
    		Node<T> input = new Node<T>(item);
    		input.next = current;
    		current.prev = input;
    		front = input;
    		size++;
    	} else if (index == this.size()) {
    		Node<T> current = back;
    		Node<T> input = new Node<T>(item);
    		input.prev = current;
    		current.next = input;
    		back = input;
    		size++;
    	} else if (index <= this.size() / 2) {
    		Node<T> current = front;
    		Node<T> input = new Node<T>(item);
    		int i = 0;
    		while (i < index) {
    			current = current.next;
    			i++;
    		}
    		Node<T> tempPrev = current.prev;
    		tempPrev.next = input;
    		input.prev = tempPrev;
    		current.prev = input;
    		input.next = current;
    		size++;
    	} else {
    		Node<T> current = back;
    		Node<T> input = new Node<T>(item);
    		int i = this.size() - 1;
    		while (i > index) {
    			current = current.prev;
    			i--;
    		}
    		Node<T> tempPrev = current.prev;
    		tempPrev.next = input;
    		input.prev = tempPrev;
    		current.prev = input;
    		input.next = current;
    		size++;
    	}
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size()) {
        	throw new IndexOutOfBoundsException();
        } else if (this.size() == 1) {
        	T temp = front.data;
        	front = null;
        	back = null;
        	size--;
        	return temp;
        } else if (index == 0) {
        	T temp = front.data;
        	Node<T> current = front.next;
        	current.prev = null;
        	front = current;
        	size--;
        	return temp;
        } else if (index == this.size() - 1) {
        	T temp = back.data;
        	Node<T> current = back.prev;
        	current.next = null;
        	back = current;
        	size--;
        	return temp;
        } else if (index <= this.size() / 2) {
        	Node<T> current = front;
        	int i = 0;
    		while (i < index) {
    			current = current.next;
    			i++;
    		}
    		T temp = current.data;
    		Node<T> before = current.prev;
    		Node<T> after = current.next;
    		before.next = after;
    		after.prev = before;
    		current.prev = null;
    		current.next = null;
    		size--;
    		return temp;    		
        } else {
        	Node<T> current = back;
    		int i = this.size() - 1;
    		while (i > index) {
    			current = current.prev;
    			i--;
    		}
    		T temp = current.data;
    		Node<T> before = current.prev;
    		Node<T> after = current.next;
    		before.next = after;
    		after.prev = before;
    		current.prev = null;
    		current.next = null;
    		size--;
    		return temp;
        }
    }

    @Override
    public int indexOf(T item) {
    	Node<T> current = front;
    	int index = 0;
    	while (index < this.size()) {
    		if (current.data == null) {
    			if (current.data == item) {
    				return index;
    			}
    		} else {	
    			if (current.data == item || current.data.equals(item)) {
	    			return index;
	    		}
    		}
    		index++;
    		current = current.next;
    	}
    	return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
    	Node<T> current = front;
    	int index = 0;
    	while (index < this.size()) {
    		if (current.data == null) {
    			if (current.data == other) {
    				return true;
    			}
    		} else {
	    		if (current.data == other || current.data.equals(other)) {
	    			return true;
	    		}
    		}
    		index++;
    		current = current.next;
    	}
    	return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
        	return current != null; 
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (hasNext()) {
            	T temp = current.data;
            	this.current = current.next;
            	return temp;
            } else {
            	throw new NoSuchElementException();
            }
        }
    }
}
