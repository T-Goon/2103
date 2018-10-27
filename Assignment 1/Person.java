import java.util.*;

public class Person extends LivingThing{
  private ArrayList<Pet> _pets =  new ArrayList<Pet>();
  private ArrayList<Possession> _possession =  new ArrayList<Possession>();

  public Person(String name, Image img){
    super(name, img);
  }

  public void setPossessions(ArrayList possessions){
    this._possession = possessions;
  }

  public void setPets (ArrayList pets){
    this._pets = pets;
  }
}
