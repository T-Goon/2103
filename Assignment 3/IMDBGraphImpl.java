import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.regex.Pattern;

public class IMDBGraphImpl implements IMDBGraph{
  private final Map<String, GraphNode> _actors;
  private final Map<String, GraphNode> _movies;
  final static Pattern START = Pattern.compile("----			------");

  /**
    * @param actorsFilename path to actors.list file
    * @param actressesFilename path to actresses.list file
    */
  public IMDBGraphImpl(String actorsFilename, String actressesFilename)throws IOException {
  // Load data from the specified actorsFilename and actressesFilename ...
  this._actors = new HashMap<String, GraphNode>();
  this._movies = new HashMap<String, GraphNode>();
  this.constructGraph(actorsFilename, this.START);
  this.constructGraph(actressesFilename, this.START);

  // remove all actors that have not been in any movies
  this.cleanActors();
  }

  /**
    * Creates/Adds on to the graph of actors and movies
    * @param path path to the file to the parsed
    * @param start pattern that indicates where the data starts
    */
  private void constructGraph(String path, Pattern start)throws FileNotFoundException{
    Scanner scanner = new Scanner(new File(path), "ISO-8859-1");
    IMDBGraphImpl.skipFileHeader(scanner, start);//Now currently on the first name
    // next skip line will return line with first name

    // constructs graph with actors file
    this.parseAndSet(scanner);
  }

  /**
    * Parses the data and adds onto the graph
    * @param s scanner object that contains the data to be parsed
    */
  private void parseAndSet(Scanner s){
    // Keeps track of current actor in file so that the movies can be added
    GraphNode currentActor = null;

    while(true){ // Loop through lines of file until the end
      try{ // catches error when scanner reaches the end of the file
        final String line = s.nextLine();

        // Important indexes for parsing the file
        final int firstTabIndex = line.indexOf("\t");
        final int lastTabIndex = line.lastIndexOf("\t") + 1;
        final int firstCloseParenIndex = line.indexOf(")", lastTabIndex) + 1;

        // get the names of actors and add them to the graph
        currentActor = this.addActor(firstTabIndex, line, currentActor);

        // find a movie's name+year if one is in the line
        final String movieNameYear = findMovie(lastTabIndex, firstCloseParenIndex, line);
        final GraphNode currentMoviesNode = (GraphNode)this.getMovie(movieNameYear);

        // if it is a movie add it to the graph and the current actors movies
        if(!movieNameYear.equals("")){
          GraphNode m;
          // if movie is not alread in the graph create its node and add it
          // to the graph
          if(currentMoviesNode ==  null ){
            m = new GraphNode(movieNameYear);
            this._movies.put(movieNameYear, m);
          }
          else{ // Movie is already in graph
            m = currentMoviesNode;
          }

          // if the current movie does not already have the current actor
          // add it to the movie's neighbors
          if(!m.getNeighbors().contains(currentActor)){
            m.addNeighbor(currentActor);
          }

          // add the movie to the current movie to the current actor's
          // neighbors
          currentActor.addNeighbor(m);
        }

      }
      catch(NoSuchElementException e){
        // end of file so break out of while loop
        break;
      }
    }
  }

  /**
    * Finds the acctors name in the current line
    * @param firstTabIndex the index of the first tab character in the line
    * @param list the current line of the file
    * @param currentActor the most recent actor in the file
    * @return the next actor or the existing one
    */
  private GraphNode addActor(int firstTabIndex, String line, GraphNode currentActor){
    if(firstTabIndex != 0 && firstTabIndex != -1){ // the line of the file has an actor
      final String actorName = line.substring(0, firstTabIndex);

      GraphNode p =  new GraphNode(actorName);
      this._actors.put(actorName, p);
      return p;
    }
    else{ // there is no actor in the line
      return currentActor;
    }
  }

  /**
    * returns a movies's name and year in the passed in line of the file and
    * empty string if it is a TV series or TV movie
    * @param lastTabIndex the indes of the last tab index plus one in line
    * @param firstCloseParenIndex the index of the first close parenthise plus one in line
    * @param line line of file where a movie name/year can be found
    * @return the movie name or empty string if the line has no movies
    */
  private static String findMovie(int lastTabIndex, int firstCloseParenIndex, String line){
    // get the name of the movie/TV show
    final String vidString = line.substring(lastTabIndex, firstCloseParenIndex);

    // filter out TV shows and TV movies
    if(vidString.indexOf("\"") == -1 && vidString.indexOf("(TV)") == -1)
       return vidString;
    else // there is no movie in the line
      return "";
  }

  /**
    * Skips the header of the data file passed in based on the start pattern
    * @param s scanner object that has the header to be skipped
    * @param start pattern that inicates the header has ended
    */
  private static void skipFileHeader(Scanner s, Pattern start){
    // Skips all the lines until the table header it reached
    while(s.findInLine(start) == null){
      s.nextLine();
    }
  }

  /**
    * Removes the actors in the graph that have not been in any movies
    */
  private void cleanActors(){
    // adds all actors in the HashMap and add them to an ArrayList
    final List<GraphNode> actors = new ArrayList<GraphNode>();
    actors.addAll(this._actors.values());

    // linear search through all actors
    for(int i=actors.size()-1;i>=0;i--){
      // if the actors has no neighbors(only been in TV shows/movies)
      // remove them from the graph
      if(actors.get(i).getNeighbors().isEmpty()){
        this._actors.remove(actors.get(i).getName());
      }
    }
  }

  /**
	 * Gets all the actor nodes in the graph.
	 * @return a collection of all the actor and actress nodes in the graph.
	 */
	public Collection<? extends Node> getActors (){
    return this._actors.values();
  }

  /**
   * Gets all the movie nodes in the graph.
   * @return a collection of all the movie nodes in the graph.
   */
  public Collection<? extends Node> getMovies (){
    return this._movies.values();
  }

  /**
   * Returns the movie node having the specified name.
   * @param name the name of the requested movie
   * @return the movie node associated with the specified name or null
   *         if no such movie exists.
   */
  public Node getMovie (String name){
        return this._movies.get(name);
  }

  /**
   * Returns the actor node having the specified name.
   * @param name the name of the requested actor
   * @return the actor node associated with the specified name or null
   *         if no such actor exists.
   */
  public Node getActor (String name){
        return this._actors.get(name);

  }
}
