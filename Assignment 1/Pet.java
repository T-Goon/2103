public class Pet extends LivingThing{
  private Person _owner;

  public Pet(String name, Image img){
    super(name, img);
  }

  public void setOwner(Person owner){
    this._owner = owner;
  }
}
