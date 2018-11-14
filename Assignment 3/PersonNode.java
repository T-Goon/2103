import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class PersonNode implements Node{
  private final String _name;
  private final Map<String, MovieNode> _movies;

  public PersonNode(String name){
    this._name = name;
    this._movies = new HashMap<String, MovieNode>();
  }

  /**
   * Returns the name of the node (e.g., "Judy Garland").
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
    return this._movies.values();
  }

  /**
    * Adds a movie to the persons movie list
    * @param movie movie to be added to the list
    */
  public void addMovie(MovieNode movie){
    this._movies.put(movie.getName(), movie);
  }

  /**
    * returns the name of the person
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
