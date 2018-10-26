public class Entity{
  private String name;
  private Image img;

  public Entity(String name, Image img){
    this.name = name;
    this.img = img;
  }

  public String getName(){
    return name;
  }

  public Image getImage(){
    return img;
  }

  public boolean equals (Object o){
    if(this.name.equals(o.toString()))
      return true;
    else{
      return false;
    }
  }

  public String toString(){
    return name;
  }
}
