import java.awt.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.lang.Math;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball {
	// Constants
	/**
	 * The radius of the ball.
	 */
	public static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	public static final double INITIAL_VX = 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 1e-7;

	/**
		* The amount each speed of the ball increases by when an animal is removed
		* from the game.
		*/
	private static final double SPEEDINCREMENT = .075;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;

	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle () {
		return circle;
	}

	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball () {
		this.x = GameImpl.WIDTH/2;
		this.y = GameImpl.HEIGHT/2;
		this.vx = INITIAL_VX;
		this.vy = INITIAL_VY;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime) {
		double dx = vx * deltaNanoTime;
		double dy = vy * deltaNanoTime;
		x += dx;
		y += dy;

		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
	}

	/**
		* @return the y position of the center of the ball
		*/
	public double getY(){
		return this.y;
	}

	/**
		* Changes the x velocity of the ball to the pass in value.
		* @param newVX the value vx is going to be set to.
		*/
	public void setVX(double newVX){
		this.vx = newVX;
	}

	/**
		* Changes the y velocity of the ball to the pass in value.
		* @param newVY the value vy is going to be set to.
		*/
	public void setVY(double newVY){
		this.vy = newVY;
	}

	/**
		* @return the current x velocity of the ball
		*/
	public double getVX(){
		return this.vx;
	}

	/**
		* @return the current y velocity of the ball
		*/
	public double getVY(){
		return this.vy;
	}

	/**
		* Handles the collision bewteen the ball and an animal.
		* @param a the animal that the ball is colliding with
		*/
	public void collide(Animal a){
		// Calculate the distance bewteen the center of the ball and
		// each of the 4 points on the animal. The one that it is nearest to
		// is the side that the ball collides with.

		// Calculate the distances between the ball and each of the 4 points in the
		// other object. Then store them in an array.
		final double[] distances = new double[4];
		final double[] top = a.getTop();
		final double[] bot = a.getBot();
		final double[] right = a.getRight();
		final double[] left = a.getLeft();
		distances[0] = findDistance(top[0], top[1]);
		distances[1] = findDistance(bot[0], bot[1]);
		distances[2] = findDistance(right[0], right[1]);
		distances[3] = findDistance(left[0], left[1]);

		// Find the min distance
		final double minDistance = Math.min(Math.min(distances[0], distances[1]),
		Math.min(distances[2], distances[3]));

		// depending on which distance was the minimum change the velocity of the ball
		if(minDistance == distances[0])
				this.vy = -Math.abs(this.vy);
		else if(minDistance == distances[1])
				this.vy = Math.abs(this.vy);
		else if(minDistance == distances[2])
				this.vx = Math.abs(this.vx);
		else if(minDistance == distances[3])
				this.vx = -Math.abs(this.vx);

	}

	/**
		* Find the distance between the center of this ball and a point.
		* @param x the x coordinate of the other point
		* @param y the y coordinate of the other point
		*/
	private double findDistance(double x, double y){
		return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
	}

	/**
		* Increases each component of the velocity by a percentage.
		*/
	public void increaseSpeed(){
		this.vx = this.vx * (1 + this.SPEEDINCREMENT);
		this.vy = this.vy * (1 + this.SPEEDINCREMENT);
	}
}
