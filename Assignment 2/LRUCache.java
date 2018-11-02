import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U>{
	private HashMap<T, Element> _storage;
	private Element<T, U> _firstElement; // First element of linked list
	private Element<T, U> _lastElement; // Last element of linked list
	private final DataProvider<T, U> _provider;
	private final int _capacity; // max size if HashMap
	private static final float LOAD_FACTOR = 1; // So that HashMap does not resize itself
	private int _numMisses;

	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		this._storage = new HashMap<T, Element>(capacity, LRUCache.LOAD_FACTOR);
		this._provider = provider;
		this._capacity = capacity;
		this._numMisses = 0;
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		U result;
		Element<T, U> holdingElement = (Element<T, U>)this._storage.get(key); // TODO unchecked cast find out how to fix

		if(holdingElement == null){ // cache miss
			result = (U)this._provider.get(key);
			this.storeResult(key, result);
			this._numMisses++;
		}
		else{ // cache hit
			result = holdingElement._value;
			// Moves called element to the front of the list
			holdingElement._leftElement._rightElement = holdingElement._rightElement;
			// Don't do if called element is already in the back of the list
			if(holdingElement._rightElement != null)
				holdingElement._rightElement._leftElement = holdingElement._leftElement;
			this.add(holdingElement);
		}

		return result;
	}

	// Creates new elements in HashMap and LinkedList
	// If linked list if full remove the least recently used value (first element)
	private void storeResult(T key, U result){
		Element<T, U> e = new Element<T, U>(key, result);

		if(this._storage.size() == this._capacity){ // HashMap is full
			this._storage.remove(this._firstElement.getKey());
			this._firstElement = this._firstElement.getRightElement();
			this._firstElement.setLeftElement(null);
		}

		this._storage.put(key, e);
		this.add(e);
	}

	// adds node to end of linked list
	private void add(Element<T, U> e){
		if(this._firstElement == null){ // Special size 0 case
			this._firstElement = e;
			this._lastElement = e;
		}
		else{ // Generic addition to end of linked list
			e._leftElement = this._lastElement;
			e._rightElement = null;
			this._lastElement._rightElement = e;
			this._lastElement = e;
		}
}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return this._numMisses;
	}

	public class Element<K, E>{
	  private Element<K, E> _leftElement;
	  private Element<K, E> _rightElement;
	  private E _value;
	  private K _key;

	  protected Element(K key, E value){
	    this._value = value;
	  }

	  protected E getValue(){
	    return this._value;
	  }

	  protected K getKey(){
	    return this._key;
	  }

	  protected void setLeftElement(Element<K, E> e){
	    this._leftElement = e;
	  }

	  protected Element<K, E> getLeftElement(){
	    return this._leftElement;
	  }

	  protected void setRightElement(Element<K, E> e){
	    this._rightElement = e;
	  }

	  protected Element<K, E> getRightElement(){
	    return this._rightElement;
	  }
	}
}
