public class FriendRequest{
  private LivingThing a;
  private LivingThing b;
  private boolean[] approved = {false, false};

  public FriendRequest(LivingThing a, LivingThing b){
    this.a = a;
    this.b = b;
  }

  public void approve(LivingThing a){
    if(this.a.equals(a)){
      approved[0] = true;
    }
    else if(this.b.equals(a)){
      approved[1] = true;
    }
    else{
      throw new IllegalArgumentException();
    }

    this.checkApproval();
  }

  private void checkApproval(){
    if(approved[0] && approved[1]){
      a.addFriend(b);
      b.addFriend(a);
    }
  }
}
