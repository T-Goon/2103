import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class GraphPartialTester {
	IMDBGraph imdbGraph;
	GraphSearchEngine searchEngine;

	/**
	 * Verifies that there is no shortest path between a specific and actor and actress.
	 */
	@Test(timeout=5000)
	public void findShortestPath () throws IOException {
		imdbGraph = new IMDBGraphImpl("actors_first_10000_lines.list", "actresses_first_10000_lines.list");
		Node actor1 = imdbGraph.getActor("Aav, �lari");
		Node actor2 = imdbGraph.getActor("Aatsalo, Johanna");
		List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
		assertNull(shortestPath);  // there is no path between these people

		// test actors in same file
		actor1 = imdbGraph.getActor("-, Donte");
		actor2 = imdbGraph.getActor("-, Jamil");
		shortestPath = searchEngine.findShortestPath(actor1, actor2);
		List<Node> result = new ArrayList<Node>();
		result.add(actor1);
		result.add(imdbGraph.getMovie("Bound Boys (2015)"));
		result.add(actor2);
		assertEquals(result, shortestPath);

		// test that actors in TV shows/movies have been removed
		actor1 = imdbGraph.getActor("-, Donte");
		actor2 = imdbGraph.getActor("$, Claw");
		shortestPath = searchEngine.findShortestPath(actor1, actor2);
		assertNull(shortestPath);

		// test path longer then 3
		actor1 = imdbGraph.getActor("'Draico' Johnson, Dondraico");
		actor2 = imdbGraph.getActor("'Spax' Szulc-Vollmann, Rafael");
		shortestPath = searchEngine.findShortestPath(actor1, actor2);
		result = new ArrayList<Node>();
		result.add(actor1);
		result.add(imdbGraph.getMovie("The LXD: The Secrets of the Ra (2011)"));
		result.add(imdbGraph.getActor("'Casper' Brown, Jesse"));
		result.add(imdbGraph.getMovie("Battle of the Year (2013)"));
		result.add(actor2);
		assertEquals(result, shortestPath);

		// test cross file paths
		actor1 = imdbGraph.getActor("Aavik, Evald");
		actor2 = imdbGraph.getActor("Aaving, Kerttu");
		shortestPath = searchEngine.findShortestPath(actor1, actor2);
		result = new ArrayList<Node>();
		result.add(actor1);
		result.add(imdbGraph.getMovie("Naerata ometi (1985)"));
		result.add(actor2);
		assertEquals(result, shortestPath);
	}

	@Before
	/**
	 * Instantiates the graph
	 */
	public void setUp () throws IOException {
		imdbGraph = new IMDBGraphImpl("actors_first_10000_lines.list", "actresses_first_10000_lines.list");
		searchEngine = new GraphSearchEngineImpl();
	}

	@Test
	/**
	 * Just verifies that the graphs could be instantiated without crashing.
	 */
	public void finishedLoading () {
		assertTrue(true);
		// Yay! We didn't crash
	}

	@Test
	/**
	 * Verifies that a specific movie has been parsed.
	 */
	public void testSpecificMovie () {
		testFindNode(imdbGraph.getMovies(), "Teaching Teaching & Understanding Understanding (2006)");
		assertNull(imdbGraph.getMovie("\"Outrageous Acts of Science\" (2012)")); // TV show has been removed
		assertNull(imdbGraph.getMovie("De gebroken kruik (1958) (TV)")); // TV movie has been removed
	}

	@Test
	/**
	 * Verifies that a specific actress has been parsed.
	 */
	public void testSpecificActress () {
		testFindNode(imdbGraph.getActors(), "Aasia");
		assertNull(imdbGraph.getActor("$, Claw")); // actor that has only been in
		// TV shows are removed
	}

	/**
	 * Verifies that the specific graph contains a node with the specified name
	 * @param graph the IMDBGraph to search for the node
	 * @param name the name of the Node
	 */
	private static void testFindNode (Collection<? extends Node> nodes, String name) {
		boolean found = false;
		for (Node node : nodes) {
			if (node.getName().trim().equals(name)) {
				found = true;
			}
		}
		assertTrue(found);
	}
}
