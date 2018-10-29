import java.util.*;

public class LivingThing extends Entity{
  private ArrayList<LivingThing> _friends = new ArrayList<LivingThing>();
  private ArrayList<Moment> _moments =  new ArrayList<Moment>();

  public LivingThing(String name, Image img){
    super(name, img);
  }

  public void setFriends (ArrayList friends){
    this._friends = friends;
  }

  public ArrayList getFriends (){
    return this._friends;
  }

  public void addFriend (LivingThing friend){
    this._friends.add(friend);
  }

  public void setMoments (ArrayList moments){
    this._moments = moments;
  }

  public Moment getOverallHappiestMoment (){
    if(this._moments.isEmpty())
      return null;

    // set initial happiest moment to the first one in the ArrayList
    Moment happiestMoment = this._moments.get(0);
    float happiestMomentAverageSmileValue = this.average(this._moments.get(0).getSmileValues());

    for(int i=1;i<this._moments.size();i++){
      // average happiness of moments besides the first
      float average = this.average(this._moments.get(i).getSmileValues());

      if(average > happiestMomentAverageSmileValue){
        happiestMomentAverageSmileValue = average;
        happiestMoment = this._moments.get(i);
      }
    }
    return happiestMoment;
  }

  // Averages an ArrayList of smileValues
  private static float average(ArrayList<Float> lst){
    float sum = 0f;
    int n = 0;
    for(int i=0;i<lst.size();i++){
      sum += lst.get(i);
      n++;
    }

    return sum/(float)n;
  }

  public LivingThing getFriendWithWhomIAmHappiest (){
    // No friends
    if(this._friends.isEmpty()){
      return null;
    }

    // List to hold lists of:
    //[LivingThing, smileValue, smileValue,...]
    // which are friends of this LivingThing
    ArrayList<ArrayList> friendsInMoment =  new ArrayList<ArrayList>();

    for(int i=0; i<this._moments.size(); i++){

      for(int j=0; j<this._moments.get(i).getParticipants().size();j++){
        // Each person in each moment
        LivingThing participant = (LivingThing)this._moments.get(i).getParticipants().get(j);
        // The smile value of this person in the moment
        float smile = findOwnSmileValue(this._moments.get(i).getParticipants(),
        this._moments.get(i).getSmileValues());

        // Make sure other person in moment is a friend
        if (this._friends.contains(participant)){
          // Check if friend is already in list friendsInMoment
          if(this.isin(friendsInMoment,participant)){
            // find friend and add the smile value to the end of the friends
            //row
            for(int k=0;k<friendsInMoment.size();k++){
              if(participant.equals(friendsInMoment.get(k).get(0))){
                friendsInMoment.get(k).add(smile);
                break;
              }
            }
          }
          // if frind is not in list f add friend and a smile value to list f
          else{
            ArrayList row = new ArrayList();
            row.add(participant);
            row.add(smile);

            friendsInMoment.add(row);
          }
        }
      }
    }
    // If no friends are in the moments then return null
    if(friendsInMoment.isEmpty())
      return null;
    //average the smile values in rows of f
    //find max average smile value and return
    return (LivingThing)this.maxSmile(this.averageRows(friendsInMoment)).get(0);
  }

  /*
  Takes in an ArrayList of ArrayLists. Each embedded ArrayList is in the form:
  [LivingThing,Float,Float,Float, ...]
  Ex. -
  [
  [LivingThing,Float,Float,Float, ...],
  [LivingThing,Float,Float,Float, ...],
  [LivingThing,Float,Float,Float, ...],
  ...
  ]

  Averages all of the floats in each row and returns
  [
  [LivingThing,Float],
  [LivingThing,Float],
  [LivingThing,Float],
  ...
  ]
  */
  private static ArrayList averageRows(ArrayList<ArrayList> lst){
    // result ArrayList
    ArrayList res = new ArrayList();

    // calculate the average
    for(int i=0;i<lst.size();i++){
      float n = 0;
      float sum = 0;

      for(int j=1;j<lst.get(i).size();j++){
        sum += (float)lst.get(i).get(j);
        n++;
      }
      // create row and add it to result list
      ArrayList row =  new ArrayList();
      row.add(lst.get(i).get(0));
      row.add(sum/n);
      res.add(row);
    }

    return res;
  }

  /*
  Takes in ArrayList of form:
  [
  [LivingThing,Float],
  [LivingThing,Float],
  [LivingThing,Float],
  ...
  ]

  Returns the LivingThing/smileValue pair maximum float value out of all the rows.
  */
  private static ArrayList maxSmile(ArrayList<ArrayList> lst){
    ArrayList max = new ArrayList();
    // default values
    max.add(new Person("",new Image("")));
    max.add(-9999.0f);

    for(int i=0;i<lst.size();i++){
        if((float)lst.get(i).get(1) > (float)max.get(1)){
          max = lst.get(i);
        }
    }

    return max;
  }

  /*
  Takes in two ArrayLists which are the participants and smileValues from a
  moment and returns the smile value of this abject or -1.0f if it is not in
  the moment.
  */
  private float findOwnSmileValue(ArrayList<LivingThing> participants,
  ArrayList<Float> smileValues){

    for(int i=0;i<participants.size();i++){
      if(participants.get(i).equals(this)){
        return (float)smileValues.get(i);
      }
    }
    return -1.0f;
  }

  /*
  Takes in a ArrayList in the form of:
  [
  [LivingThing,Float,Float,Float, ...],
  [LivingThing,Float,Float,Float, ...],
  [LivingThing,Float,Float,Float, ...],
  ...
  ]
  and a LivingThing.

  Returns true if the LivingThing is in the ArrayList's rows, else false.
  */
  private static boolean isin(ArrayList<ArrayList> lst, LivingThing a){
    for(int i=0;i<lst.size();i++){
      if(lst.get(i).get(0).equals(a)){
        return true;
      }
    }
    return false;
  }

  public static boolean isClique(ArrayList<LivingThing> set){
    int setSize = set.size();
    for(int i=0;i<setSize;i++)
      for(int j=0;j<setSize;j++)
        if(!(i == j) && !set.get(i).getFriends().contains(set.get(j)))
          return false;
    return true;
  }

  /*
  Takes in a ArrayList and returns a portion of that ArrayList from first
  inclusive to last exclusive.

  Same as ArrayList.subList(int first, int last) but returns type ArrayList.
  */
  private static ArrayList getRange(ArrayList array, int first, int last){
    ArrayList result = new ArrayList();
    for(int i=first;i<last;i++)
      result.add(array.get(i));

    return result;
  }

  public ArrayList findMaximumCliqueOfFriends(){

    for(int i=this._friends.size();i>0;i--){
      int first = 0;
      int last = i;
      for(int j=0;j<=this._friends.size()-i;j++){
        ArrayList subList = LivingThing.getRange(this._friends, first, last);
        if(LivingThing.isClique(subList))
          return subList;
        first++;
        last++;
      }
    }

    return new ArrayList();
  }

}
