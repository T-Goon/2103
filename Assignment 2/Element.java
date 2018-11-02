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
