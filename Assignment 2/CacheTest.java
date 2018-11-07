import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
	final private DataProvider<Integer,String> _provider = new StringGenerator<Integer,String>();
	final private Cache<Integer,String> _cache = new LRUCache<Integer,String>(this._provider, 5);

	@Test(expected=IllegalArgumentException.class)
	public void illogicalCacheSize(){
		final DataProvider<Integer,String> provider = new StringGenerator<Integer,String>();
		final Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 0);
	}

	@Test
	public void getIsCorrectForCacheMiss(){
		assertEquals("0(\\*U*/)", this._cache.get(0));
	}

	@Test
	public void getIsCorrectForCacheHit(){
		this._cache.get(0);
		assertEquals("0(\\*U*/)", this._cache.get(0));
	}

	@Test
	public void leastRecentlyUsedIsCorrect() { // also tests getNumMisses
		this._cache.get(0); //miss
		this._cache.get(1); //miss
		this._cache.get(2); //miss
		this._cache.get(3); //miss
		this._cache.get(4); //miss
		this._cache.get(5); //miss, remove 0
		this._cache.get(0); //miss, remove 1
		assertEquals(7, this._cache.getNumMisses());

		this._cache.get(2); //hit,
												//may cause null pointer exception
												//when calling least recently used value
		assertEquals(7, this._cache.getNumMisses());

		this._cache.get(2); //hit, may cause null pointer exception to call most recently
												//used value
		assertEquals(7, this._cache.getNumMisses());

		this._cache.get(6); //miss, if not remove 3 here then forgot to reassign least recently used
												//value when hit is on the current least recently used value

		this._cache.get(3); //miss
		assertEquals(9, this._cache.getNumMisses());

		this._cache.get(0); //hit, move from middle to end of list
		this._cache.get(7); //miss
		this._cache.get(8); //miss
		this._cache.get(9); //miss
		this._cache.get(10); //miss
		assertEquals(13, this._cache.getNumMisses());
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

	@Test
	public void cacheOfSizeOne(){
		final DataProvider<Integer,String> provider = new StringGenerator<Integer,String>();
		final Cache<Integer,String> cache = new LRUCache<Integer,String>(this._provider, 1);
		cache.get(0); //miss
		cache.get(1); //miss
		cache.get(0); //miss
		cache.get(0); //hit
		cache.get(0); //hit
		assertEquals(3, cache.getNumMisses());
	}

	@Test
	public void nullTest(){
		this._cache.get(null); //miss
		this._cache.get(0); //miss
		this._cache.get(1); //miss
		this._cache.get(2); //miss
		this._cache.get(3); //miss
		this._cache.get(4); //miss
		this._cache.get(5); //miss, remove 0
		this._cache.get(0); //miss, remove 1
		this._cache.get(null); //miss
		assertEquals(9, this._cache.getNumMisses());
	}
}
