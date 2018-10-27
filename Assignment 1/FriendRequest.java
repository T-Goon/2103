public class FriendRequest{
  private LivingThing _livingThing1;
  private LivingThing _livingThing2;
  private boolean[] _approved = {false, false};

  public FriendRequest(LivingThing a, LivingThing b){
    this._livingThing1 = a;
    this._livingThing2 = b;
  }

  public void approve(LivingThing approver){
    if(this._livingThing1.equals(approver)){
      _approved[0] = true;
    }
    else if(this._livingThing2.equals(approver)){
      _approved[1] = true;
    }
    else{
      throw new IllegalArgumentException();
    }

    this.checkApproval();
  }

  private void checkApproval(){
    if(_approved[0] && _approved[1]){
      _livingThing1.addFriend(_livingThing2);
      _livingThing2.addFriend(_livingThing1);
    }
  }
}
