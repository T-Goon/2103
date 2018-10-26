public class Possession extends Entity{
  private Person owner;
  private float price;

  public Possession(String name, Image image, float price){
      super(name, image);
      this.price = price;
  }

  public void setOwner(Person owner){
    this.owner = owner;
  }

  public float getPrice(){
    return price;
  }
}
