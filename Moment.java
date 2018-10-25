import java.util.*;

public class Moment extends Entity{
  private ArrayList<LivingThing> participants;
  private ArrayList<Float> smileValues;

  public Moment(String name, Image img, ArrayList participants, ArrayList smileValues){
    super(name, img);
    this.participants = participants;
    this.smileValues = smileValues;
  }

  public ArrayList getParticipants(){
    return participants;
  }

  public ArrayList getSmileValues(){
    return smileValues;
  }
}
