import java.util.*;

public class LivingThing extends Entity{
  private ArrayList<LivingThing> friends = new ArrayList<LivingThing>();
  private ArrayList<Moment> moments;

  public LivingThing(String name, Image img){
    super(name, img);
  }

  public void setFriends (ArrayList friends){
    this.friends = friends;
  }

  public ArrayList getFriends (){
    return this.friends;
  }

  public void addFriend (LivingThing friend){
    this.friends.add(friend);
  }

  public void setMoments (ArrayList moments){
    this.moments = moments;
  }

  public LivingThing getFriendWithWhomIAmHappiest (){
    // List to hold lists of:
    //[LivingThing, smileValue, smileValue,...]
    ArrayList<ArrayList> f =  new ArrayList<ArrayList>();

    for(int i=0; i<moments.size(); i++){

      for(int j=0; j<moments.get(i).getParticipants().size();j++){
        // Each person in each moment
        LivingThing p = (LivingThing)moments.get(i).getParticipants().get(j);
        // The smile value of this person in the moment
        float s = findOwnSmileValue(moments.get(i).getParticipants(),
        moments.get(i).getSmileValues());

        // Make sure other person in moment is a friend
        if (friends.contains(p)){
          // Check if friend is already in list f
          if(this.isin(f,p)){
            // find friend and add the smile value to the end of the friends
            //row
            for(int k=0;k<f.size();k++){
              if(p.equals(f.get(k).get(0))){
                f.get(k).add(s);
                break;
              }
            }
          }
          // if frind is not in list f add friend and a smile value to list f
          else{
            ArrayList t = new ArrayList();
            t.add(p);
            t.add(s);

            f.add(t);
          }
        }
      }

    }
    //average the smile values in rows of f
    //find max average smile value and return
    return (LivingThing)this.maxSmile(this.averageRows(f)).get(0);
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
  private  ArrayList averageRows(ArrayList<ArrayList> a){
    ArrayList res = new ArrayList();

    for(int i=0;i<a.size();i++){
      float n = 0;
      float sum = 0;
      for(int j=1;j<a.get(i).size();j++){
        sum += (float)a.get(i).get(j);
        n++;
      }
      ArrayList temp =  new ArrayList();
      temp.add(a.get(i).get(0));
      temp.add(sum/n);
      res.add(temp);
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

  Returns the maximum float value out of all the rows.
  */
  private ArrayList maxSmile(ArrayList<ArrayList> a){
    ArrayList max = new ArrayList();
    max.add(new Person("",new Image("")));
    max.add(-9999.0f);

    for(int i=0;i<a.size();i++){
        if((float)a.get(i).get(1) > (float)max.get(1)){
          max = a.get(i);
        }
    }

    return max;
  }

  /*
  Takes in two ArrayLists which are the participants and smileValues from a
  moment and returns the smile value of this abject or -1.0f if it is not in
  the moment.
  */
  private float findOwnSmileValue(ArrayList<LivingThing> a, ArrayList<Float> s){
    for(int i=0;i<a.size();i++){
      if(a.get(i).equals(this)){
        return (float)s.get(i);
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
  private boolean isin(ArrayList<ArrayList> a, LivingThing b){
    for(int i=0;i<a.size();i++){
      if(a.get(i).get(0).equals(b)){
        return true;
      }
    }
    return false;
  }
}
