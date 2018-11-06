public class StringGenerator<T, U> implements DataProvider<T, String>{

  public StringGenerator(){

  }


  /**
   * @param a object of type T
   * @return return value of a's toString() concatinated to (\*U* /)
   */
  public String get(T a){
    return a.toString() + "(\\*U*/)";
  }
}
