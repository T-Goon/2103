import java.util.*;

public class Moment extends Entity{
  private ArrayList<LivingThing> _participants;
  private ArrayList<Float> _smileValues;

  public Moment(String name, Image img, ArrayList participants, ArrayList smileValues){
    super(name, img);
    this._participants = participants;
    this._smileValues = smileValues;
  }

  public ArrayList getParticipants(){
    return this._participants;
  }

  public ArrayList getSmileValues(){
    return this._smileValues;
  }
}
