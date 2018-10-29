public class Entity{
  private String _name;
  private Image _img;

  public Entity(String name, Image img){
    this._name = name;
    this._img = img;
  }

  public String getName(){
    return this._name;
  }

  public Image getImage(){
    return this._img;
  }

  public boolean equals (Object o){
    if(this._name.equals(o.toString()))
      return true;
    else{
      return false;
    }
  }

  public String toString(){
    return this._name;
  }

}
