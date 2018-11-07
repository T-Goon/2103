import java.util.*;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U>{
	private Map<T, Element> _storage;
	private Element<T, U> _firstElement; // First element of linked list
	private Element<T, U> _lastElement; // Last element of linked list
	private final DataProvider<T, U> _provider;
	private final int _capacity; // max size if HashMap
	private int _numMisses;

	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		if(capacity < 1)
			throw new IllegalArgumentException("Capacity must be greater than or equal to 1.");

		this._storage = new HashMap<T, Element>(capacity);
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
		final Element<T, U> holdingElement = (Element<T, U>)this._storage.get(key);

		if(holdingElement == null){ // cache miss
			// Get data from provider and add to cache
			result = (U)this._provider.get(key);
			this.storeResult(key, result);
			this._numMisses++;
		}
		else{ // cache hit, Moves called element to the front of the list
			result = holdingElement._value;

			// Don't do if holdingElement is the _firstElement
			if(holdingElement._leftElement != null){
				holdingElement._leftElement._rightElement = holdingElement._rightElement;
				// if most recently used value is called move _lastElement back one
				if(holdingElement._rightElement == null){
					this._lastElement = holdingElement._leftElement;
				}
			}
			else{ // Reassign _firstElement when it is moved to back of LinkedList
				this._firstElement = this._firstElement._rightElement;
			}
			// Don't do if called element is already in the back of the list
			if(holdingElement._rightElement != null){
				holdingElement._rightElement._leftElement = holdingElement._leftElement;
			}
			this.add(holdingElement);
		}

		return result;
	}

	/**
	 * Creates new elements in HashMap and LinkedList
	 * If HashMap is full remove the least recently used key-value pair,(first element)
	 * @param key new nodes key
	 * @param result the LinkedList node that the HashMap points to
	 */
	private void storeResult(T key, U result){
		final Element<T, U> e = new Element<T, U>(key, result);

		if(this._storage.size() >= this._capacity){ // HashMap is full
			// Remove least recently used value and reassign the next
			// least recently used value
			this._storage.remove(this._firstElement._key);

			if(this._capacity > 1){
				this._firstElement = this._firstElement._rightElement;
				this._firstElement._leftElement = null;
			}

		}

		this._storage.put(key, e);
		this.add(e);
	}

	/**
	 * adds node to end of linked list
	 * @param e elemnt to be added to the end of the LinkedList
	 */
	private void add(Element<T, U> e){
		if(this._storage.size() <= 1){ // Special size 0/1 case
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

	// Elements of the linked list
	public class Element<K, E>{
	  private Element<K, E> _leftElement;
	  private Element<K, E> _rightElement;
	  private E _value;
	  private K _key;

	  private Element(K key, E value){
	    this._value = value;
			this._key = key;
	  }

	}
}
