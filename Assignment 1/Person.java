import java.util.*;

public class Person extends LivingThing{
  private ArrayList<Pet> pets =  new ArrayList<Pet>();
  private ArrayList<Possession> possession =  new ArrayList<Possession>();

  public Person(String name, Image img){
    super(name, img);
  }

  public void setPossessions(ArrayList possessions){
    this.possession = possessions;
  }

  public void setPets (ArrayList pets){
    this.pets = pets;
  }
}
