package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.Searcher;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
	IDictionary<V, ISet<E>> maze;
	IList<V> vertices;
	IList<E> edges;
	int numVertices;
	int numEdges;
	
    public Graph(IList<V> vertices, IList<E> edges) {
    	maze = new ChainedHashDictionary<>();
    	this.vertices = vertices;
    	this.edges = new DoubleLinkedList<>();
    	numVertices = vertices.size();
    	numEdges = 0;
    	boolean hasCheck = false;
    	boolean hasCount = false;
    	for (V vertex : vertices) {
    		ISet<E> conEdges = new ChainedHashSet<>();
    		for (E edge : edges) {
    			if (edge.getWeight() < 0) {
    				throw new IllegalArgumentException();
    			}
    			
    			V conVertex1 = edge.getVertex1();
    			V conVertex2 = edge.getVertex2();
    			
    			if (!hasCheck) {
	    			if (!vertices.contains(conVertex1) || !vertices.contains(conVertex2)) {
	    				throw new IllegalArgumentException();
	    			}
    			}

    			if (!hasCount) {
    				numEdges++;
    			}
    			
    			if (vertex.equals(conVertex1) || vertex.equals(conVertex2)) {
    				conEdges.add(edge);
    			}
    		}
    		
    		for (E edge : conEdges) {
    			if (!this.edges.contains(edge)) {
    				this.edges.add(edge);
    			}
    		}
    		
    		hasCheck = true;
    		hasCount = true;
    		maze.put(vertex, conEdges);
    	}
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return numVertices;
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
    	return numEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
    	IDisjointSet<V> mst1Set= new ArrayDisjointSet<>();
        for(V vertex : vertices) {
            mst1Set.makeSet(vertex);
        }
        
        IList<E> top = Searcher.topKSort(edges.size(), this.edges);
        ISet<E> mst1 = new ChainedHashSet<>();
        
        for(E edge : top) {
            if(mst1Set.findSet(edge.getVertex1()) != mst1Set.findSet(edge.getVertex2())){
                mst1Set.union(edge.getVertex1(), edge.getVertex2());
                mst1.add(edge);                
            }
        }
        
        return mst1;

    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
    	// IList<E> paths = new DoubleLinkedList<>();
    	if (start.equals(end)) {
    		return new DoubleLinkedList<>();
    	}    	
    	
    	ISet<V> settledVertices = new ChainedHashSet<>();
    	ISet<V> unSettledVertices = new ChainedHashSet<>();
    	IDictionary<V, E> predecessor = new ChainedHashDictionary<>();
    	IDictionary<V, IPriorityQueue<Double>> distances = new ChainedHashDictionary<>();
    	
    	// Mark every vertices as unsettled.
    	// Every distance from start to of each vertex is set to infinity.
    	for (V vertex : vertices) {
    		unSettledVertices.add(vertex);
    		IPriorityQueue<Double> distance = new ArrayHeap<>();
        	distance.insert(Double.POSITIVE_INFINITY);
    		distances.put(vertex, distance);
    	}
    	
    	// Set the distance from start vertex to itself to 0
    	distances.get(start).insert(0.0);
    	
    	while (unSettledVertices.size() != 0) {
    		V closestVertex = getClosestVertex(unSettledVertices, distances);
    		if (closestVertex.equals(end)) {
				return getPath(end, predecessor);
			}
			
    		ISet<E> connectEdges = findConnectEdges(closestVertex, unSettledVertices);
    		if (connectEdges.size() <= 0) {
    			throw new NoPathExistsException();
    		}
    		
    		double currentDistance = distances.get(closestVertex).peekMin();
    		for (E edge : connectEdges) {
    			V connectVertex = edge.getOtherVertex(closestVertex);
    			double edgeWeight = edge.getWeight();	
    			double nextDistance = distances.get(connectVertex).peekMin();
    			if (currentDistance + edgeWeight < nextDistance) {
    				distances.get(connectVertex).insert(currentDistance + edgeWeight);
    				predecessor.put(connectVertex, edge);
    			}
    		}
    		unSettledVertices.remove(closestVertex);
    		settledVertices.add(closestVertex);
    	}
        return getPath(end, predecessor);
    }
    
    private V getClosestVertex(ISet<V> unSettledVertices, IDictionary<V, IPriorityQueue<Double>> distances) {
    	V closestVertex = null;
    	double shortestDistance = Double.POSITIVE_INFINITY;
    	
    	for (V vertex : unSettledVertices) {
    		double distance = distances.get(vertex).peekMin();
    		if (shortestDistance > distance) {
    			shortestDistance = distance;
    			closestVertex = vertex;
    		}
    		
    	}
    	
    	return closestVertex;
    }
        
    private ISet<E> findConnectEdges(V current, ISet<V> unSettledVertices) {
    	ISet<E> connectEdges = new ChainedHashSet<>();
    	for (E edge : edges) {
    		V vertex1 = edge.getVertex1();
    		V vertex2 = edge.getVertex2();
    		// if the edge is not self loop and contains the current vertex
    		if ((!vertex1.equals(vertex2)) && (vertex1.equals(current) || vertex2.equals(current))) {
    			connectEdges.add(edge);
    		}
    	}
    	
    	return connectEdges;
    	
    }
    
    private IList<E> getPath(V target, IDictionary<V, E> predecessor) {
    	IList<E> paths = new DoubleLinkedList<>();
    	
    	for (int i = 0; i < predecessor.size(); i++) {
    		E current = predecessor.get(target);
    		V next = current.getOtherVertex(target);
    		paths.add(current);
    		target = next;
    	}
    	return paths;
    }
}
