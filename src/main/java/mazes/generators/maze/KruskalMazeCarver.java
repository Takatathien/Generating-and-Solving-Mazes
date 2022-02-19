package mazes.generators.maze;

import java.util.Random;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import misc.graphs.Graph;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

    	ISet<Room> rooms = maze.getRooms();
    	ISet<Wall> walls = maze.getWalls();
    	
    	makeRandomWalls(walls);
    	Graph map = new Graph(rooms, walls);
    	ISet<Wall> toRemove = map.findMinimumSpanningTree();
    	restoreWalls(walls);
    	return toRemove;
    }
    
    private void makeRandomWalls(ISet<Wall> inputWall) {
    	Random rand = new Random();
    	for (Wall wall : inputWall) {
    		double randWeight = 10 * rand.nextDouble();
    		wall.setDistance(randWeight);
    	}    	
    }
    
    private void restoreWalls(ISet<Wall> inputWall) {
    	for (Wall wall : inputWall) {
    		wall.resetDistanceToOriginal();
    	}
    }
}
