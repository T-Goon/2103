import java.util.*;

public class PersonNode implements Node{
  private final String _name;
  private final List<MovieNode> _movies;

  public PersonNode(String name){
    this._name = name;
    this._movies = new ArrayList<MovieNode>();
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
    return this._movies;
  }

  /**
    * Adds a movie to the persons movie list
    * @param movie movie to be added to the list
    */
  public void addMovie(MovieNode movie){
    this._movies.add(movie);
  }

  /**
    * returns the name of the person
    */
  public String toString(){
    return this._name;
  }
}
