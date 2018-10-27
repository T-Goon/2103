public class Possession extends Entity{
  private Person _owner;
  private float _price;

  public Possession(String name, Image image, float price){
      super(name, image);
      this._price = price;
  }

  public void setOwner(Person owner){
    this._owner = owner;
  }

  public float getPrice(){
    return this._price;
  }
}
