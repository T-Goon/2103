import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

public class IMDBGraphImpl implements IMDBGraph{
  private final List<PersonNode> _actors;
  private final List<MovieNode> _movies;
  final static Pattern ACTORSSTART = Pattern.compile("THE ACTORS LIST");
  final static Pattern ACCTRESSESSTART = Pattern.compile("THE ACTRESSES LIST");
  final static int NUMOFEXTRALINESAFTERTITLE = 6;

  /**
    * @param actorsFilename path to actors.list file
    * @param actressesFilename path to actresses.list file
    */
  public IMDBGraphImpl(String actorsFilename, String actressesFilename)throws IOException {
  // Load data from the specified actorsFilename and actressesFilename ...
  this._actors = new ArrayList<PersonNode>();
  this._movies = new ArrayList<MovieNode>();
  this.constructGraph(actorsFilename, this.ACTORSSTART);
  this.constructGraph(actressesFilename, this.ACCTRESSESSTART);
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
    PersonNode currentActor = null;

    while(true){ // Loop through lines of file until the end
      try{
        final String line = s.nextLine();

        final int firstTabIndex = line.indexOf("\t");
        final int lastTabIndex = line.lastIndexOf("\t") + 1;
        final int firstCloseParenIndex = line.indexOf(")") + 1;

        // get the names of actors and add them to the list
        if(firstTabIndex != 0 && firstTabIndex != -1){
          final String actorName = line.substring(0, firstTabIndex);
          //System.out.println(actorName);
          PersonNode p =  new PersonNode(actorName);
          this._actors.add(p);
          currentActor = p;
        }

        // find a move name/year if one is in the line
        final String movieNameYear = findMovie(lastTabIndex, firstCloseParenIndex, line);
        final MovieNode currentMoviesNode = (MovieNode)this.getMovie(movieNameYear);
        // if it is a movie add it to the list and the current actors movies
        if(!movieNameYear.equals("")){
          MovieNode m;
          // if movie is not alread in the list create its node and add it
          if(currentMoviesNode ==  null ){
            m = new MovieNode(movieNameYear);
            this._movies.add(m);
          }
          else{
            m = currentMoviesNode;
          }

          if(!m.getNeighbors().contains(currentActor))
            m.addActor(currentActor);
          currentActor.addMovie(m);
        }

      }
      catch(NoSuchElementException e){
        break;
      }
    }
  }

  /**
    * returns a movies's name and year in the passed in line of the file and
    * empty string if it is a TV series or TV movie
    * @param lastTabIndex the indes of the last tab index plus one in line
    * @param firstCloseParenIndex the index of the first close parenthise plus one in line
    * @param line line of file where a movie name/year can be found
    */
  private static String findMovie(int lastTabIndex, int firstCloseParenIndex, String line){
    final String vidString = line.substring(lastTabIndex, firstCloseParenIndex);
    if(vidString.indexOf("\"") == -1 && vidString.indexOf("TV") == -1)
       return vidString;
    else
      return "";
  }

  /**
    * Skips the header of the data file passed in based on the start pattern
    * @param s scanner object that has the header to be skipped
    * @param start pattern that inicates the header has ended
    */
  private static void skipFileHeader(Scanner s, Pattern start){
    while(s.findInLine(start) == null){
      s.nextLine();
    }
    for(int i=0;i<IMDBGraphImpl.NUMOFEXTRALINESAFTERTITLE;i++)
      //System.out.println(s.nextLine());
      s.nextLine();
  }

  /**
	 * Gets all the actor nodes in the graph.
	 * @return a collection of all the actor and actress nodes in the graph.
	 */
	public Collection<? extends Node> getActors (){
    return this._actors;
  }

  /**
   * Gets all the movie nodes in the graph.
   * @return a collection of all the movie nodes in the graph.
   */
  public Collection<? extends Node> getMovies (){
    return this._movies;
  }

  /**
   * Returns the movie node having the specified name.
   * @param name the name of the requested movie
   * @return the movie node associated with the specified name or null
   * if no such movie exists.
   */
  public Node getMovie (String name){
    for(int i=0;i<this._movies.size();i++){
      if(this._movies.get(i).getName().equals(name)){
        return this._movies.get(i);
      }
    }
    return null;
  }

  /**
   * Returns the actor node having the specified name.
   * @param name the name of the requested actor
   * @return the actor node associated with the specified name or null
   * if no such actor exists.
   */
  public Node getActor (String name){
    for(int i=0;i<this._actors.size();i++){
      if(this._actors.get(i).getName().equals(name)){
        return this._actors.get(i);
      }
    }
    return null;
  }
}
