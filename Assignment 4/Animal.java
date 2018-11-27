import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class Animal{
  private final Label _img;
  // coordiantes of the four points between each pair of corners in the square image
  private final double[] _top, _bot, _right, _left;
  private final String _name;

  public Animal(Image img, double x, double y, String name){
    // Create label and put in correct position
    final Label imageLabel = new Label("", new ImageView(img));
    imageLabel.setLayoutX(x - img.getWidth()/2);
    imageLabel.setLayoutY(y - img.getHeight()/2);

    // Store instance variables
    this._img = imageLabel;
    this._name = name;
    this._top = new double[]{x, (y - (img.getHeight()/2))};
    this._bot = new double[]{x, (y + (img.getHeight()/2))};
    this._right = new double[]{(x + (img.getWidth()/2)), y};
    this._left = new double[]{(x - (img.getWidth()/2)), y};
  }

  /**
    * @return the JavaFX label that contains the image
    */
  public Label getAnimal(){
    return this._img;
  }

  /**
    * @return coordiantes (x,y) for the point between the top 2 corners of the
    * image
    */
  public double[] getTop(){
    return this._top;
  }

  /**
    * @return coordiantes (x,y) for the point between the bottom 2 corners of the
    * image
    */
  public double[] getBot(){
    return this._bot;
  }

  /**
    * @return coordiantes (x,y) for the point between the right 2 corners of the
    * image
    */
  public double[] getRight(){
    return this._right;
  }

  /**
    * @return coordiantes (x,y) for the point between the left 2 corners of the
    * image
    */
  public double[] getLeft(){
    return this._left;
  }

  /**
    * @return the name of the animals image
    */
  public String getName(){
    return this._name;
  }
}
