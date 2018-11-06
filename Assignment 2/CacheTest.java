import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
	//TODO @Before -init


	@Test(expected=IllegalArgumentException.class)
	public void illogicalCacheSize(){
		final DataProvider<Integer,String> provider = new StringGenerator<Integer,String>();
		final Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 0);
	}

	@Test
	public void leastRecentlyUsedIsCorrect() { // also tests getNumMisses
		final DataProvider<Integer,String> provider = new StringGenerator<Integer,String>();
		final Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		cache.get(0); //miss
		cache.get(1); //miss
		cache.get(2); //miss
		cache.get(3); //miss
		cache.get(4); //miss
		cache.get(5); //miss, remove 0
		cache.get(0); //miss, remove 1
		cache.get(2); //hit, if not remove 3 here then forgot to reassign least recently used
									//value when hit is on the current least recently used value
									//can cause null pointer exception
		assertEquals(7, cache.getNumMisses());
		cache.get(2); //hit, can cause null pointer exception to call most recently
									//used value
		cache.get(6); //miss
		cache.get(3); //miss

		assertEquals(9, cache.getNumMisses());
	}

	@Test
	public void getIsCorrectForCacheHit(){
		final DataProvider<Integer,String> provider = new StringGenerator<Integer,String>();
		final Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		cache.get(0);
		assertEquals("0(\\*U*/)", cache.get(0));
	}

	@Test
	public void getIsCorrectForCacheMiss(){
		final DataProvider<Integer,String> provider = new StringGenerator<Integer,String>();
		final Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		assertEquals("0(\\*U*/)", cache.get(0));
	}

	@Test
	public void keysOfDifferentTypesTest(){
		final DataProvider provider = new StringGenerator();
		final Cache cache = new LRUCache(provider, 5);
		cache.get(cache);    //miss
		cache.get(1);        //miss
		cache.get(2);        //miss
		cache.get(3f);       //miss
		cache.get(4);        //miss
		cache.get(5.0);      //miss
		cache.get(cache);    //miss
		cache.get(2);        //hit
		cache.get(provider); //miss
		cache.get("3");      //miss

		assertEquals(9, cache.getNumMisses());
	}
}
