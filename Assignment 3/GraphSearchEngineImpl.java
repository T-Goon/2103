import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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
    if(s == null || t == null){
      return null;
    }

    boolean found = false;
    Map<Node, Integer> distances = new HashMap<Node, Integer>();
    List<Node> visitedNodes = new ArrayList<Node>();
    List<Node> nodesToVisit = new ArrayList<Node>();
    nodesToVisit.add(s);
    distances.put(s, 0);
    while (nodesToVisit.size() > 0) {
      Node n = nodesToVisit.remove(0);
      visitedNodes.add(n);

      if(n.equals(t)){
        found = true;
        break;
      }

      for (Node neighbor : n.getNeighbors()){
        if (!nodesToVisit.contains(neighbor) && !visitedNodes.contains(neighbor)){
          nodesToVisit.add(neighbor);
          distances.put(neighbor, distances.get(n)+1);
        }
      }
    }

    if(!found){
      return null;
    }

    List<Node> path = new ArrayList<Node>();
    int count = distances.get(t);
    Node current = t;
    path.add(t);
    while(count > 0){
      for(Node n : current.getNeighbors()){
        if(distances.get(n) != null && distances.get(n) == count--){
          path.add(0, n);
          count--;
          current = n;
          break;
        }
      }
    }
    path.add(0, s);

    return path;
  }

}
