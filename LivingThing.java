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
    ArrayList<ArrayList> f =  new ArrayList<ArrayList>();

    for(int i=0; i<moments.size(); i++){

      for(int j=0; j<moments.get(i).getParticipants().size();j++){
        LivingThing p = (LivingThing)moments.get(i).getParticipants().get(j);
        float s = (float)moments.get(i).getSmileValues().get(j);

          if (friends.contains(p)){
            if(f.contains(p)){
              for(int k=0;k<f.size();k++){
                if(f.get(k).get(0).equals(p)){
                  f.get(k).add(s);
                }
              }
            }
          }
            else{
              ArrayList t = new ArrayList();
              t.add(p);
              t.add(s);

              f.add(t);
            }
      }
    }

    //TODO
    // average the numbers in each f f =[[person,f,f,f],[pet,f,f,f]]
    //return person/pet with the highest average
    return new LivingThing("bob", new Image("BOB.jpg"));
  }
}
