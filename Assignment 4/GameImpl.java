import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.scene.image.Image;
import java.lang.Math;

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

	// Instance variables
	private Ball ball;
	private Paddle paddle;

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
		getChildren().clear();  // remove all components from the game

		// Create and add ball
		ball = new Ball();
		getChildren().add(ball.getCircle());  // Add the ball to the game board

		// Create and add animals ...TODO
		double xCord = GameImpl.XYINITIAL;
		double yCord = GameImpl.XYINITIAL;
		for(int i=0;i<GameImpl.NUMOFANIMALS;i++){

			final Animal a = new Animal(new Image(getClass().getResourceAsStream(
			GameImpl.FILENAMES[(int)(Math.random() * GameImpl.FILENAMES.length)])),
			xCord, yCord);

			getChildren().add(a.getAnimal());

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

		// Add another event handler to steer paddle...TODO
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
	public GameState runOneTimestep (long deltaNanoTime) { //TODO
		ball.updatePosition(deltaNanoTime);

		// Ball has collided with paddle TODO the ball can pass through the paddle and can also get stuck in the middle
		if(this.ball.getCircle().getBoundsInParent().intersects(this.paddle.getRectangle().getBoundsInParent())){
			// Check if the y position of the center of the ball is below the center of the paddle
			// if yes then the ball has collided with the bottom of the paddle
			if(this.ball.getY() >= this.paddle.getCenterY()){
				this.ball.setVY(Math.abs(this.ball.getVY()));
				System.out.println(1);
			}
			// Check if the y position of the center of the ball is above the center of the paddle
			// if yes then the ball has collided with the top of the paddle
			else if(this.ball.getY() <= this.paddle.getCenterY()){
				this.ball.setVY(-Math.abs(this.ball.getVY()));
				System.out.println(2);
			}
		}

		// Ball is colliding with the right side of the screen
		if(this.ball.getCircle().getBoundsInParent().intersects(this.WIDTH, 0, this.BOXWIDTH, this.HEIGHT)){
			this.ball.setVX(-Math.abs(this.ball.getVX()));
		}
		// Ball is colliding with the left side of the screen
		else if(this.ball.getCircle().getBoundsInParent().intersects(-this.BOXWIDTH, 0, this.BOXWIDTH, this.HEIGHT)){
			this.ball.setVX(Math.abs(this.ball.getVX()));
		}
		// Ball is colliding with the top of the screen
		else if(this.ball.getCircle().getBoundsInParent().intersects(0, -this.BOXWIDTH, this.WIDTH, this.BOXWIDTH)){
			this.ball.setVY(Math.abs(this.ball.getVY()));
		}
		// Ball is colliding with the bottom of the screen
		else if(this.ball.getCircle().getBoundsInParent().intersects(0, this.HEIGHT, this.WIDTH, this.BOXWIDTH)){
			this.ball.setVY(-Math.abs(this.ball.getVY()));
		}

		// Check if the ball has collided with any oter object each tick
		//for()

		return GameState.ACTIVE;
	}
}
