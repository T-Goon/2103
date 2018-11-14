import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class MovieNode implements Node{
  private final String _nameYear;
  private final Map<String, PersonNode> _actors;

  public MovieNode(String nameYear){
    this._nameYear = nameYear;
    this._actors = new HashMap<String, PersonNode>();
  }

  /**
	 * Returns the name of the node (e.g., "Judy Garland").
	 * @return the name of the Node.
	 */
	public String getName (){
    return this._nameYear;
  }

  /**
   * Returns the Collection of neighbors of the node.
   * @return the Collection of all the neighbors of this Node.
   */
  public Collection<? extends Node> getNeighbors (){
    return this._actors.values();
  }

  public void addActor(PersonNode p){
    this._actors.put(p.getName(), p);
  }

  /**
    * returns the name of the movie
    */
  public String toString(){
    return this._nameYear;
  }

  /**
    * Compares 2 nodes
    * @return true if they are equal and false otherwise
    */
  public boolean equals(Node n){
    return this._nameYear.equals(n.getName());
  }
}
