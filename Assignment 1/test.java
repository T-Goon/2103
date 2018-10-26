import java.util.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class test{
  public static void main(String[] args){
    Entity a = new Entity("Bob", new Image("Bob.png"));
    System.out.print(a.getName());

    LivingThing b = new LivingThing("Joe", new Image("Joe.png"));
    ArrayList<Entity> bFriends = new ArrayList<Entity>();
    bFriends.add(a);
    b.setFriends(bFriends);
    System.out.print(b.getFriends());

    LivingThing c = new LivingThing("Mike", new Image("Mike.png"));
    b.addFriend(c);

    System.out.println(b.getFriends());



        Result result = JUnitCore.runClasses(FacebukPartialTester.class);
        System.out.print("Test has been run successfully.\n");

        for (Failure failure : result.getFailures()){
          System.out.print(failure.toString()+"\n");
        }

        if(result.wasSuccessful()){
          System.out.print("All Tests Passed!");
        }
      }

    }
