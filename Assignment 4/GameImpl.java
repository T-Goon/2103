import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.scene.image.Image;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class GameImpl extends Pane implements Game {
	/**
	 * Defines different states of the game.
	 */
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;

	/**
		* Number of animals on the game board.
		*/
	private final static int NUMOFANIMALS = 16;

	/**
		* Files names of animial images.
		*/
	private final static String[] FILENAMES = {"duck.jpg", "goat.jpg", "horse.jpg"};

	/**
		* Coordiante boundary for where to stop placing animals
		*/
	private final static int BOUNDARY = GameImpl.WIDTH - 50;

	/**
		* x and y amount of space between animals and the inial
		* coordiante for the first animal
		*/
	private final static int XSPACE = 100, YSPACE = 70, XYINITIAL = 50;

	/**
		* Width/height of the edge-of-screen boundary box
		*/
	private final static int BOXWIDTH = 30, BOXHEIGHT = 0;

	/**
		* The number of times the ball must hit the bottom of the screen before the
		* player loses the game.
		*/
	private final static int LOSECONDITION = 5;

	// Instance variables
	private Ball ball;
	private Paddle paddle;
	private final List<Animal> animals =  new ArrayList<Animal>();
	private int loseCounter = 0;

	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl () {
		setStyle("-fx-background-color: white;");

		restartGame(GameState.NEW);
	}

	public String getName () {
		return "Zutopia";
	}

	public Pane getPane () {
		return this;
	}

	private void restartGame (GameState state) {
		this.animals.clear();
		getChildren().clear();  // remove all components from the game
		this.loseCounter = 0;

		// Create and add ball
		ball = new Ball();
		getChildren().add(ball.getCircle());  // Add the ball to the game board

		// Create and add animals ...
		double xCord = GameImpl.XYINITIAL;
		double yCord = GameImpl.XYINITIAL;
		for(int i=0;i<GameImpl.NUMOFANIMALS;i++){
			final int rand = (int)(Math.random() * GameImpl.FILENAMES.length);

			// Create a new animal with a random image of goat, horse, or duck
			final Animal a = new Animal(new Image(getClass().getResourceAsStream(
			GameImpl.FILENAMES[rand])),
			xCord, yCord, GameImpl.FILENAMES[rand]);

			// adds new animal to scene and animal list
			getChildren().add(a.getAnimal());
			this.animals.add(a);

			// increment positions so that animals are placed correctly
			if(xCord == GameImpl.BOUNDARY){
				xCord = GameImpl.XYINITIAL;
				yCord += GameImpl.YSPACE;
			}
			else{
				xCord += GameImpl.XSPACE;
			}
		}

		// Create and add paddle
		paddle = new Paddle();
		getChildren().add(paddle.getRectangle());  // Add the paddle to the game board

		// Add start message
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);

		// Add event handler to start the game
		this.setOnMouseClicked(new EventHandler<MouseEvent> () {
			@Override
			public void handle (MouseEvent e) {
				GameImpl.this.setOnMouseClicked(null);

				// As soon as the mouse is clicked, remove the startLabel from the game board
				getChildren().remove(startLabel);
				run();
			}
		});

		// Add another event handler to steer paddle...
		this.setOnMouseMoved(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent t){
				paddle.moveTo(t.getSceneX(), t.getSceneY());
			}
		});
	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run () {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	/**
	 * Updates the state of the game at each timestep. In particular, this method should
	 * move the ball, check if the ball collided with any of the animals, walls, or the paddle, etc.
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	public GameState runOneTimestep (long deltaNanoTime) {

		ball.updatePosition(deltaNanoTime);

		// Ball has collided with paddle
		if(this.ball.getCircle().getBoundsInParent().intersects(this.paddle.getRectangle().getBoundsInParent())){
			// Check if the y position of the center of the ball is below the center of the paddle
			// if yes then the ball has collided with the bottom of the paddle
			if(this.ball.getY() >= this.paddle.getCenterY()){
				this.ball.setVY(Math.abs(this.ball.getVY()));
			}
			// Check if the y position of the center of the ball is above the center of the paddle
			// if yes then the ball has collided with the top of the paddle
			else if(this.ball.getY() <= this.paddle.getCenterY()){
				this.ball.setVY(-Math.abs(this.ball.getVY()));
			}
			this.playSound("");
		}

		if(!this.collideWithWall())
			return GameState.LOST;

		Animal toRemove = null;
		// Check if the ball has collided with any of the animals each tick
		for(Animal a : this.animals){
			// The ball has collided with an animal
			if(this.ball.getCircle().getBoundsInParent().intersects(a.getAnimal().getBoundsInParent())){
				this.ball.collide(a);
				this.ball.increaseSpeed();
				toRemove = a;
				this.getChildren().remove(a.getAnimal());
				this.playSound(toRemove.getName());
			}
		}

		// remove the animial that the ball has collided with
		this.animals.remove(toRemove);

		// all the animals are gone, game is won
		if(this.animals.isEmpty()){
			this.playSound("win");
			return GameState.WON;
		}

		return GameState.ACTIVE;
	}

	/**
		* Play the a sound based off of what is passed in as name.
		* @param name keyword for what sound is going to be played
		*/
		public static synchronized void playSound(final String name) {
		  new Thread(new Runnable() {
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();

						// Select audio file based off of name
						AudioInputStream inputStream;
						if(name.equals("goat.jpg"))
		        	inputStream = AudioSystem.getAudioInputStream(
		          	getClass().getResourceAsStream("Goat-noise.wav"));
						else if(name.equals("horse.jpg"))
							inputStream = AudioSystem.getAudioInputStream(
								getClass().getResourceAsStream("whinny.wav"));
						else if(name.equals("duck.jpg"))
							inputStream = AudioSystem.getAudioInputStream(
								getClass().getResourceAsStream("quack.wav"));
						else if(name.equals("win"))
							inputStream = AudioSystem.getAudioInputStream(
								getClass().getResourceAsStream("chaching.wav"));
						else if(name.equals("lose"))
							inputStream = AudioSystem.getAudioInputStream(
								getClass().getResourceAsStream("shatter.wav"));
						else
							inputStream = AudioSystem.getAudioInputStream(
								getClass().getResourceAsStream("boing.wav"));

		        clip.open(inputStream);
		        clip.start();
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}

		/**
			* Handle the collision of the ball with the sides of the game board.
			* @return false if the game has ended and true otherwise
			*/
		public boolean collideWithWall(){
			// Ball is colliding with the right side of the screen
			if(this.ball.getCircle().getBoundsInParent().intersects(this.WIDTH, 0, this.BOXWIDTH, this.HEIGHT)){
				this.ball.setVX(-Math.abs(this.ball.getVX()));
				this.playSound("");
			}
			// Ball is colliding with the left side of the screen
			else if(this.ball.getCircle().getBoundsInParent().intersects(-this.BOXWIDTH, 0, this.BOXWIDTH, this.HEIGHT)){
				this.ball.setVX(Math.abs(this.ball.getVX()));
				this.playSound("");
			}
			// Ball is colliding with the top of the screen
			else if(this.ball.getCircle().getBoundsInParent().intersects(0, -this.BOXWIDTH, this.WIDTH, this.BOXWIDTH)){
				this.ball.setVY(Math.abs(this.ball.getVY()));
				this.playSound("");
			}
			// Ball is colliding with the bottom of the screen
			else if(this.ball.getCircle().getBoundsInParent().intersects(0, this.HEIGHT, this.WIDTH, this.BOXWIDTH)){
				this.ball.setVY(-Math.abs(this.ball.getVY()));
				this.playSound("");
				loseCounter++;
				// Lost the game
				if(loseCounter == this.LOSECONDITION){
					this.playSound("lose");
					return false;
				}
			}

			return true;
		}
}
