public class Pet extends LivingThing{
  private Person owner;

  public Pet(String name, Image img){
    super(name, img);
  }

  public void setOwner(Person owner){
    this.owner = owner;
  }
}
