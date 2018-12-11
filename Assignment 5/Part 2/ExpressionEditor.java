import javafx.application.Application;
import java.util.*;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ExpressionEditor extends Application {
	public static void main (String[] args) {
		launch(args);
	}

	/**
	 * Mouse event handler for the entire pane that constitutes the ExpressionEditor
	 */
	private static class MouseEventHandler implements EventHandler<MouseEvent> {//TODO
		static private Pane _p;
		static private CompoundExpression _root;
		static Node _focus;
		static boolean _hasBeenDragged = false;

		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			MouseEventHandler._p = pane_;
			MouseEventHandler._root = rootExpression_;
			MouseEventHandler._focus = null;
		}

		public void handle (MouseEvent event) {
			final double x = event.getSceneX();
			final double y = event.getSceneY();

			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

			}
			else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				// The mouse has not been dragged so this is a click
				if(!MouseEventHandler._hasBeenDragged){
					MouseEventHandler.setBorder("");
					MouseEventHandler._focus =  MouseEventHandler.findFocus(x, y);
					MouseEventHandler.setBorder("-fx-border-color: red;");
					System.out.println(MouseEventHandler._focus);
				}
				// The mouse was previously dragged so this is a drag and release
				else{
					System.out.println(0);
					MouseEventHandler._hasBeenDragged = false;
				}
			}
			else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				System.out.println(2);
				MouseEventHandler._hasBeenDragged = true;
			}

		}

		/**
			* Find the focus in the expression based on where the user clicked.
			* @param x the x position of where the user clicked.
			* @param y the y position of where the user clicked.
			*/
		private static Node findFocus(double x, double y){
			// Check if there is no focus and if the position of the mouse is in the expression's bounds
			if(MouseEventHandler._focus == null &&
			MouseEventHandler._root.getNode().localToScene(MouseEventHandler._root.getNode().getBoundsInLocal()).contains(x, y)){
				return MouseEventHandler._root.getNode();
			}

			Pane focus = null;
			// Make sure the current focus is a pane so that it will have children
			if(MouseEventHandler._focus instanceof Pane){
				focus = (Pane)MouseEventHandler._focus;
			}
			// If a focus if a pane check if the user clicked on of its children
			if(focus != null){
				for(Node n : focus.getChildren()){
					if(n.localToScene(n.getBoundsInLocal()).contains(x, y)){
						// If you click on a label make sure that it is not a +/*/(/) label
						if(!(n instanceof Label)){
								return n;
						}
						else{
							final Label l = (Label)n;
							if(!(l.getText().equals("+") || l.getText().equals("*") ||
							l.getText().equals(")") || l.getText().equals("("))){
								return n;
							}
						}
					}
				}
			}

			return null;

		}

		/**
			* Change the style value of the focus if it is not null.
			* @param css The string representing the css style
			*/
		private static void setBorder(String css){
			if(MouseEventHandler._focus != null)
				MouseEventHandler._focus.setStyle(css);

		}

	}

	/**
	 * Size of the GUI
	 */
	private static final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 250;

	/**
	 * Initial expression shown in the textbox
	 */
	private static final String EXAMPLE_EXPRESSION = "2*x+3*y+4*z+(7+6*z)";

	/**
	 * Parser used for parsing expressions.
	 */
	private final ExpressionParser expressionParser = new SimpleExpressionParser();

	@Override
	public void start (Stage primaryStage) {
		primaryStage.setTitle("Expression Editor");

		// Add the textbox and Parser button
		final Pane queryPane = new HBox();
		final TextField textField = new TextField(EXAMPLE_EXPRESSION);
		final Button button = new Button("Parse");
		queryPane.getChildren().add(textField);

		final Pane expressionPane = new Pane();

		// Add the callback to handle when the Parse button is pressed
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle (MouseEvent e) {
				// Try to parse the expression
				try {
					// Success! Add the expression's Node to the expressionPane
					final Expression expression = expressionParser.parse(textField.getText(), true);
					System.out.println(expression.convertToString(0));
					expressionPane.getChildren().clear();
					expressionPane.getChildren().add(expression.getNode());
					expression.getNode().setLayoutX(WINDOW_WIDTH/4);
					expression.getNode().setLayoutY(WINDOW_HEIGHT/3);

					// If the parsed expression is a CompoundExpression, then register some callbacks
					if (expression instanceof CompoundExpression) {
						((Pane) expression.getNode()).setBorder(Expression.NO_BORDER);
						final MouseEventHandler eventHandler = new MouseEventHandler(expressionPane, (CompoundExpression) expression);
						expressionPane.setOnMousePressed(eventHandler);
						expressionPane.setOnMouseDragged(eventHandler);
						expressionPane.setOnMouseReleased(eventHandler);
					}
				} catch (ExpressionParseException epe) {
					// If we can't parse the expression, then mark it in red
					textField.setStyle("-fx-text-fill: red");
				}
			}
		});
		queryPane.getChildren().add(button);

		// Reset the color to black whenever the user presses a key
		textField.setOnKeyPressed(e -> textField.setStyle("-fx-text-fill: black"));

		final BorderPane root = new BorderPane();
		root.setTop(queryPane);
		root.setCenter(expressionPane);

		primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
		primaryStage.show();
	}
}
