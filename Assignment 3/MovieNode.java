import java.util.*;

public class MovieNode implements Node{
  private final String _nameYear;
  private final List<PersonNode> _actors;

  public MovieNode(String nameYear){
    this._nameYear = nameYear;
    this._actors = new ArrayList<PersonNode>();
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
    return this._actors;
  }

  public void addActor(PersonNode p){
    this._actors.add(p);
  }

  /**
    * returns the name of the movie
    */
  public String toString(){
    return this._nameYear;
  }
}
