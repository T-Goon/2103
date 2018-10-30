import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * This is a SUBSET of the unit tests with which we will grade your code.
 *
 * Make absolute sure that your code can compile together with this tester!
 * If it does not, you may get a very low grade for your assignment.
 */
public class FacebukPartialTester {
	private Person _barack, _michelle, _kevin, _ina, _joe, _malia;
	private Pet _bo, _sunny;
	private Moment _meAndBarack;
	private ArrayList _michelleAndBarack, _michelleJoeBoAndMalia, _boList;

	@Before
	public void setUp () {
		initPeople();
		initPets();
		initGroups();
		initPossessions();
		initMoments();
		setTestVars();
	}

	private void initPeople () {
		_michelle = new Person("Michelle", new Image("Michelle.png"));
		_barack = new Person("Barack", new Image("Barack.png"));
		_kevin = new Person("Kevin", new Image("Kevin.png"));
		_ina = new Person("Ina", new Image("Ina.png"));
		_joe = new Person("Joe", new Image("Joe.png"));
		_malia = new Person("Malia", new Image("Malia.png"));
	}

	private void initPets () {
		_bo = new Pet("Bo", new Image("Bo.png"));
		_sunny = new Pet("Sunny", new Image("Sunny.png"));

		_bo.setOwner(_michelle);
		_sunny.setOwner(_michelle);
	}

	private void initGroups () {
		// Kevin, Barack, and Ina
		final ArrayList michelleFriends = new ArrayList();
		michelleFriends.add(_kevin);
		michelleFriends.add(_barack);
		michelleFriends.add(_ina);

		// Michelle and Barack
		_michelleAndBarack = new ArrayList();
		_michelleAndBarack.add(_michelle);
		_michelleAndBarack.add(_barack);

		// Michelle, Joe, Bo, and Malia
		_michelleJoeBoAndMalia = new ArrayList();
		_michelleJoeBoAndMalia.add(_michelle);
		_michelleJoeBoAndMalia.add(_joe);
		_michelleJoeBoAndMalia.add(_bo);
		_michelleJoeBoAndMalia.add(_malia);

		// Malia and Sunny
		final ArrayList maliaAndSunny = new ArrayList();
		maliaAndSunny.add(_malia);
		maliaAndSunny.add(_sunny);

		// Michelle
		final ArrayList michelleList = new ArrayList();
		michelleList.add(_michelle);

		// Bo
		_boList = new ArrayList();
		_boList.add(_bo);

		// Set people's friend lists
		_michelle.setFriends(michelleFriends);
		_malia.setFriends(_boList);
		_barack.setFriends(michelleList);
		_kevin.setFriends(michelleList);
		_ina.setFriends(michelleList);

		// Finish configuring pets
		_bo.setFriends(maliaAndSunny);
		_sunny.setFriends(_boList);
		final ArrayList boAndSunny = new ArrayList();
		boAndSunny.add(_bo);
		boAndSunny.add(_sunny);
		_michelle.setPets(boAndSunny);
	}

	private void initPossessions () {
		final Possession boxingBag = new Possession("BoxingBag", new Image("BoxingBag.png"), 20.0f);
		boxingBag.setOwner(_michelle);
		final ArrayList michellePossessions = new ArrayList();
		michellePossessions.add(boxingBag);
		_michelle.setPossessions(michellePossessions);
	}

	private void initMoments () {
		// Smiles
		final ArrayList michelleAndBarackSmiles = new ArrayList();
		michelleAndBarackSmiles.add(0.25f);
		michelleAndBarackSmiles.add(0.75f);

		final ArrayList michelleJoeBoAndMaliaSmiles = new ArrayList();
		michelleJoeBoAndMaliaSmiles.add(0.2f);
		michelleJoeBoAndMaliaSmiles.add(0.3f);
		michelleJoeBoAndMaliaSmiles.add(0.4f);
		michelleJoeBoAndMaliaSmiles.add(0.5f);

		// Moments
		_meAndBarack = new Moment("Me & Barack", new Image("MeAndBarack.png"), _michelleAndBarack, michelleAndBarackSmiles);
		final Moment meJoeAndCo = new Moment("Me, Joe & co.", new Image("MeJoeAndCo.png"), _michelleJoeBoAndMalia, michelleJoeBoAndMaliaSmiles);

		final ArrayList michelleMoments = new ArrayList();
		michelleMoments.add(_meAndBarack);
		michelleMoments.add(meJoeAndCo);
		_michelle.setMoments(michelleMoments);

		final ArrayList barackMoments = new ArrayList();
		barackMoments.add(_meAndBarack);
		_barack.setMoments(barackMoments);

		final ArrayList joeMoments = new ArrayList();
		joeMoments.add(meJoeAndCo);
		_joe.setMoments(joeMoments);

		final ArrayList maliaMoments = new ArrayList();
		maliaMoments.add(meJoeAndCo);
		_malia.setMoments(maliaMoments);

		final ArrayList boMoments = new ArrayList();
		boMoments.add(meJoeAndCo);
		_bo.setMoments(boMoments);
	}

	@Test
	public void testEquals () {
		assertEquals(_michelle, new Person("Michelle", new Image("Michelle.png")));
		assertEquals(_michelle, new Person("Michelle", new Image("Michelle2.png")));  // should still work
		assertNotEquals(_michelle, _barack);
	}

	@Test
	public void testFindBestMoment () {
		assertEquals(_michelle.getOverallHappiestMoment(), _meAndBarack);
	}

	@Test
	public void testGetFriendWithWhomIAmHappiest () {
		assertEquals(_michelle.getFriendWithWhomIAmHappiest(), _barack);
	}

	@Test
	public void testFriendRequest1 () {
		Person person1 = new Person("person1", new Image("person1.png"));
		Person person2 = new Person("person2", new Image("person2.png"));
		Pet pet1 = new Pet("pet1", new Image("pet1.png"));

		FriendRequest friendRequest1 = new FriendRequest(person1, person2);
		// Make sure the code also compiles for making friend requests for people and pets
		FriendRequest friendRequest2 = new FriendRequest(person1, pet1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFriendRequest2 () {
		Person person1 = new Person("person1", new Image("person1.png"));
		Person person2 = new Person("person2", new Image("person2.png"));
		Person person3 = new Person("person3", new Image("person3.png"));
		FriendRequest friendRequest = new FriendRequest(person1, person2);
		// This should raise an IllegalArgumentException:
		friendRequest.approve(person3);
	}

	final Person _p = new Person("", new Image(""));
	final Person _a = new Person("A", new Image("A.jpg"));
	final Person _b = new Person("B", new Image("B.jpg"));
	final ArrayList _part = new ArrayList();
	final ArrayList _part2 = new ArrayList();
	final ArrayList _part3 = new ArrayList();
	final ArrayList _smile = new ArrayList();
	final ArrayList _smile2 = new ArrayList();
	final ArrayList _smile3 = new ArrayList();

	private void setTestVars(){
		_part.add(_a);
		_part.add(_p);

		_part2.add(_p);
		_part2.add(_b);

		_part3.add(_p);
		_part3.add(_b);

		_smile.add(0f);
		_smile.add(10f);

		_smile2.add(20f);
		_smile2.add(0f);

		_smile3.add(200f);
		_smile3.add(0f);
	}

	// TODO: write more methods to test addFriend

	@Test
	public void testAddFriend(){
		_p.addFriend(_bo);
		assertEquals(true, _p.getFriends().contains(_bo));
	}

	@Test
	public void testAddFriend2(){
		_a.addFriend(_barack);
		assertEquals(true, _a.getFriends().contains(_barack));
	}

	// TODO: write more methods to test approve

	// Successful friend request approval
	@Test
	public void testApprove(){
		FriendRequest request =  new FriendRequest(_p, _a);
		request.approve(_a);
		request.approve(_p);

		assertEquals(true, _a.getFriends().contains(_p));
		assertEquals(true, _p.getFriends().contains(_a));
	}

	// TODO: write more methods to test getFriendWithWhomIAmHappiest

	// Friends tied for average happiness value, returns one of them
	@Test
	public void testGetFriendWithWhomIAmHappiest2() {
		ArrayList<Person> friends = new ArrayList<Person>();
		friends.add(_a);
		friends.add(_b);

		ArrayList<Moment> m = new ArrayList<Moment>();
		ArrayList<Person> part =  new ArrayList<Person>();
		part.add(_p);
		part.add(_a);
		part.add(_b);
		ArrayList smile = new ArrayList();
		smile.add(5f);
		smile.add(5f);
		smile.add(5f);

		m.add(new Moment("", new Image(""), part, smile));

		_p.setFriends(friends);
		_p.setMoments(m);
		assertEquals(_a, _p.getFriendWithWhomIAmHappiest());
	}

	// person has no moments
	@Test
	public void testGetFriendWithWhomIAmHappiestNull () {
		_p.setFriends(new ArrayList());
		assertEquals(_p.getFriendWithWhomIAmHappiest(), null);
	}

	// has moments and friends but friends are not in any moments
	@Test
	public void testGetFriendWithWhomIAmHappiestNull2 () {
		ArrayList<Person> friends = new ArrayList<Person>();
		friends.add(_a);

		ArrayList<Moment> m = new ArrayList<Moment>();


		m.add(new Moment("two", new Image(""), _part2, _smile2));

		_p.setFriends(friends);
		_p.setMoments(m);

		assertEquals(null, _p.getFriendWithWhomIAmHappiest());
	}

	// TODO: write more methods to test getOverallHappiestMoment

	@Test
	public void testGetOverallHappiestMoment () {
		ArrayList<Moment> m = new ArrayList<Moment>();

		m.add(new Moment("one", new Image(""), _part, _smile));

		_p.setMoments(m);

		assertEquals(m.get(0), _p.getOverallHappiestMoment());
	}

	@Test
	public void testGetOverallHappiestMoment2 () {
		ArrayList<Moment> m = new ArrayList<Moment>();

		m.add(new Moment("one", new Image(""), _part, _smile));
		m.add(new Moment("two", new Image(""), _part2, _smile2));
		m.add(new Moment("three", new Image(""), _part3, _smile3));

		_p.setMoments(m);

		assertEquals(m.get(2), _p.getOverallHappiestMoment());
	}

	@Test
	public void testGetOverallHappiestMomentNull () {
		Person _p = new Person("a",new Image(""));
		_p.setMoments(new ArrayList());

		assertEquals(null, _p.getOverallHappiestMoment());
	}

	// TODO: write methods to test isClique
	@Test
	public void testIsClique(){
		assertEquals(true, LivingThing.isClique(_michelleAndBarack));
	}

	@Test
	public void testIsClique2(){
		ArrayList empty = new ArrayList();
		assertEquals(true, LivingThing.isClique(empty));
	}

	@Test
	public void testIsClique3(){
		assertEquals(false, LivingThing.isClique(_michelleJoeBoAndMalia));
	}

	@Test
	public void testIsClique4(){
		ArrayList<LivingThing> loner = new ArrayList<LivingThing>();
		ArrayList<LivingThing> pList = new ArrayList<LivingThing>();
		_p.setFriends(pList);
		loner.add(_p);


		assertEquals(true, LivingThing.isClique(loner));
	}

	@Test
	public void testIsClique5(){
		ArrayList<LivingThing> friendsList = new ArrayList<LivingThing>();
		friendsList.add(_p);
		friendsList.add(_a);
		friendsList.add(_b);
		ArrayList<LivingThing> pFriends = new ArrayList<LivingThing>();
		pFriends.add(_a);
		pFriends.add(_b);
		ArrayList<LivingThing> aFriends = new ArrayList<LivingThing>();
		aFriends.add(_p);
		aFriends.add(_b);
		ArrayList<LivingThing> bFriends = new ArrayList<LivingThing>();
		bFriends.add(_p);
		bFriends.add(_a);
		_p.setFriends(pFriends);
		_a.setFriends(aFriends);
		_b.setFriends(bFriends);

		assertEquals(true, LivingThing.isClique(friendsList));
	}

	// TODO: write methods to test findMaximumCliqueOfFriends
	@Test
	public void testMaximumCliqueOfFriends(){

		assertEquals(_boList, _sunny.findMaximumCliqueOfFriends());
	}

	@Test
	public void testMaximumCliqueOfFriends2(){
		ArrayList kevinList = new ArrayList();
		kevinList.add(_kevin);
		assertEquals(kevinList, _michelle.findMaximumCliqueOfFriends());
	}

	@Test
	public void testMaximumCliqueOfFriendsNoFriends(){
		ArrayList empty = new ArrayList();
		assertEquals(empty, _joe.findMaximumCliqueOfFriends());
	}

	@Test
	public void testMaximumCliqueOfFriends3(){
		LivingThing  w = new LivingThing("w", new Image("w"));
		ArrayList<LivingThing> pFriends = new ArrayList<LivingThing>();
		pFriends.add(_a);
		pFriends.add(_b);
		pFriends.add(w);
		ArrayList<LivingThing> aFriends = new ArrayList<LivingThing>();
		aFriends.add(_p);
		aFriends.add(w);
		ArrayList<LivingThing> bFriends = new ArrayList<LivingThing>();
		bFriends.add(_p);
		ArrayList<LivingThing> wFriends = new ArrayList<LivingThing>();
		wFriends.add(_p);
		wFriends.add(_a);
		_p.setFriends(pFriends);
		_a.setFriends(aFriends);
		_b.setFriends(bFriends);
		w.setFriends(wFriends);

		ArrayList<LivingThing> result =  new ArrayList<LivingThing>();
		result.add(_a);
		result.add(w);
		
		assertEquals(result, _p.findMaximumCliqueOfFriends());
	}

}
