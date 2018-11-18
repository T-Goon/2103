import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class Animal{
  private final Label _img;

  public Animal(Image img, double x, double y){
    final Label imageLabel = new Label("", new ImageView(img));
    imageLabel.setLayoutX(x - img.getWidth()/2);
    imageLabel.setLayoutY(y - img.getHeight()/2);
    this._img = imageLabel;
  }

  public Label getAnimal(){
    return this._img;
  }
}
