import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class GraphSearchEngineImpl implements GraphSearchEngine{


  public GraphSearchEngineImpl(){

  }

  /**
   * Finds a shortest path, if one exists, between nodes s and
   * t. The path will be a list: (s, ..., t). If no
   * path exists, then this method will return null.
   * @param s the start node.
   * @param t the target node.
   * @return a shortest path in the form of a List of Node objects
   * or null if no path exists.
   */
  public List<Node> findShortestPath (Node s, Node t){
    // if the start or targe nodes are null there cannot be a path between them
    if(s == null || t == null){
      return null;
    }

    // do breadth first search from s to t
    final List result = (List)GraphSearchEngineImpl.breadthFirstSearch(s, t);

    // if the target has not been found return null
    if(!(boolean)result.get(0)){
      return null;
    }

    // find path from s to t
    final List<Node> path =  (List)GraphSearchEngineImpl.backTrack((Map)result.get(1), s, t);

    return path;
  }

  /**
    * Does breadth first search from node 's' to node 't'
    * @param s the starting node for the breadth first search
    * @param t the target node for the breadth first search
    * @return a collection of size 2, the first being a boolean of if the
    *         target node was found, and the second a table of distances for
    *         each node searched and its distance from node s
    */
  private static Collection breadthFirstSearch(Node s, Node t){
    boolean found = false; // keeps track of if the target was found or not
    // distances of nodes from start node
    final Map<Node, Integer> distances = new HashMap<Node, Integer>();
    // visited nodes list
    final List<Node> visitedNodes = new ArrayList<Node>();
    // queue for nodes to visit
    final List<Node> nodesToVisit = new ArrayList<Node>();
    // initialize the search and add distance from 's' to 's' to
    // distances HashMap
    nodesToVisit.add(s);
    distances.put(s, 0);

    // breadth first search
    while (nodesToVisit.size() > 0) { // while queue is not empty
      // dequeue next node and add to visitedNodes
      final Node n = nodesToVisit.remove(0);
      visitedNodes.add(n);

      // if the target has been found stop searching
      if(n.equals(t)){
        found = true;
        break;
      }

      // add current node's neighbors to queue and record thier distances from s
      for (Node neighbor : n.getNeighbors()){
        if (!nodesToVisit.contains(neighbor) && !visitedNodes.contains(neighbor)){
          nodesToVisit.add(neighbor);
          distances.put(neighbor, distances.get(n)+1);
        }
      }
    }

    // add info. required by user fn to a list and return it
    final List result = new ArrayList();
    result.add(found);
    result.add(distances);

    return result;
  }

  /**
    * Backtracks from node 't' to node 's' using a distances Map
    * and records the path
    * @param distances Map of nodes searched through and their distance from ndoe 's'
    * @param s the start node, backtracking ends here
    * @param t the target node, backtracking starts here
    */
  private static Collection<? extends Node> backTrack(Map<Node, Integer> distances,
  Node s, Node t){
    // the path from s to t
    final List<Node> path = new ArrayList<Node>();
    // number of steps t is away from s
    int count = distances.get(t);
    // initialize backtracking
    Node current = t;
    path.add(t);

    // backtrack from t to s and record the path
    while(count > 0){
      // look in current nodes neighbors for one that has been searched
      // and has distance away from s of (current node distance - 1)
      for(Node n : current.getNeighbors()){
        if(distances.get(n) != null && distances.get(n) == count-1){
          path.add(0, n); // when found add it to the path
          // set variables for the next step in the search and break out
          // of the current one
          count--;
          current = n;
          break;
        }
      }
    }

    return path;
  }
}
