import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class GraphNode implements Node{
  private final String _name;
  private final Map<String, GraphNode> _neighbors;

  public GraphNode(String name){
    this._name = name;
    this._neighbors = new HashMap<String, GraphNode>();
  }

  /**
   * Returns the name of the node 
   * @return the name of the Node.
   */
  public String getName (){
    return this._name;
  }

  /**
   * Returns the Collection of neighbors of the node.
   * @return the Collection of all the neighbors of this Node.
   */
  public Collection<? extends Node> getNeighbors (){
    return this._neighbors.values();
  }

  /**
    * Adds a node to this node's neighbors
    * @param node node to be added to the neighbors map
    */
  public void addNeighbor(GraphNode node){
    this._neighbors.put(node.getName(), node);
  }

  /**
    * returns the name of the node
    */
  public String toString(){
    return this._name;
  }

  /**
    * Compares 2 nodes
    * @return true if they are equal and false otherwise
    */
  public boolean equals(Node n){
    return this._name.equals(n.getName());
  }
}
