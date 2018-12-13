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
		static private Expression _focus;
		static private boolean _hasBeenDragged = false;
		static private Node _deepCopy;
		static private List<OpperationExpression> _configs;

		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			MouseEventHandler._p = pane_;
			MouseEventHandler._root = rootExpression_;
			MouseEventHandler._focus = null;
			MouseEventHandler._deepCopy = null;
			MouseEventHandler._configs = null;
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
					// At the end dragging a focus reset all values releated to the focus
					MouseEventHandler.resetFocusValues();
					System.out.println(MouseEventHandler._root.convertToString(0));

					MouseEventHandler._hasBeenDragged = false;
				}
			}
			else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				System.out.println(x);
				// 1.Grey out dragged node X
				// 2.Calculate the x position of where the node would go if it was moved.
				// 3.Have the mouse drag an image around. X
				// 4.Show the expression where mouse is closest to a position.
				// 5.Makse sure to edit the expresions and not just the javafx nodes

				// Make sure a focus exists
				if(MouseEventHandler._focus != null){
					// Make a deep copy to drag is one does not exist and grey out the focus
					if(MouseEventHandler._deepCopy == null){
						MouseEventHandler._focus.getNode().setOpacity(.6);
						MouseEventHandler._deepCopy = MouseEventHandler._focus.deepCopy().getNode();
						MouseEventHandler._p.getChildren().add(MouseEventHandler._deepCopy);
					}
					// Have the deep copy follow the mouse
					else{
						MouseEventHandler.moveDeepCopy(x, y);
					}

					if(MouseEventHandler._configs == null){
						MouseEventHandler._configs = MouseEventHandler.calcualteFocusParentConfigs();
						// replace focus parent with the config copy
					}
					else{
						final OpperationExpression parent = (OpperationExpression)MouseEventHandler._focus.getParent();
						Integer focusIndex = null;
						for(int i=0;i<parent.getChildren().size();i++){
							if(parent.getChildren().get(i).equals(MouseEventHandler._focus))
								focusIndex = i;
						}

						parent.moveChild(MouseEventHandler.findNearestConfigIndex(x), focusIndex);

						MouseEventHandler._p.getChildren().clear();
						final OpperationExpression root = (OpperationExpression)MouseEventHandler._root;
						root.clearNode();
						MouseEventHandler._p.getChildren().add(MouseEventHandler._root.getNode());
					}

				}

				MouseEventHandler._hasBeenDragged = true;
			}

		}

		/**
			* Find the focus in the expression based on where the user clicked.
			* @param x the x position of where the user clicked.
			* @param y the y position of where the user clicked.
			*/
		private static Expression findFocus(double x, double y){
			// Check if there is no focus and if the position of the mouse is in the expression's bounds
			if(MouseEventHandler._focus == null &&
			MouseEventHandler._root.getNode().localToScene(MouseEventHandler._root.getNode().getBoundsInLocal()).contains(x, y)){
				return MouseEventHandler._root;
			}

			// If a focus if a pane check if the user clicked on of its children
			if(MouseEventHandler._focus != null && MouseEventHandler._focus.getNode() instanceof Pane){
				OpperationExpression focus = (OpperationExpression)MouseEventHandler._focus;
				for(int i=0;i<focus.getChildren().size();i++){
					Node n = focus.getChildren().get(i).getNode();

					if(n.localToScene(n.getBoundsInLocal()).contains(x, y)){
						// If you click on a label make sure that it is not a +/*/(/) label
						if(!(n instanceof Label)){
								return focus.getChildren().get(i);
						}
						else{
							final Label l = (Label)n;
							if(!(l.getText().equals("+") || l.getText().equals("*") ||
							l.getText().equals(")") || l.getText().equals("("))){
								return focus.getChildren().get(i);
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
				MouseEventHandler._focus.getNode().setStyle(css);

		}

		/**
			* Reset values related to the focus if there is one.
			*/
		private static void resetFocusValues(){
			if(MouseEventHandler._focus != null){
				MouseEventHandler._focus.getNode().setOpacity(1);
				MouseEventHandler.setBorder("");
				MouseEventHandler._focus = null;
				MouseEventHandler._p.getChildren().remove(MouseEventHandler._deepCopy);
				MouseEventHandler._deepCopy = null;
				MouseEventHandler._configs = null;
			}
		}

		/**
			* Calculates and changes the position of the deep copy so that it follows the mouse.
			*/
		private static void moveDeepCopy(double x, double y){
			MouseEventHandler._deepCopy.setTranslateX(x - (MouseEventHandler._deepCopy.getLayoutX() +
			MouseEventHandler._deepCopy.localToScene(MouseEventHandler._deepCopy.getBoundsInLocal()).getWidth()/2));

			MouseEventHandler._deepCopy.setTranslateY(y - (MouseEventHandler._deepCopy.getLayoutY() +
			MouseEventHandler._deepCopy.localToScene(MouseEventHandler._deepCopy.getBoundsInLocal()).getHeight()*2));
		}

		/**
			* Calculates all possible configurations of
			* the focus in its parent if it has one.
			* @return all possible configurations of
			* the focus in its parent if it has one.
			*/
		private static List<OpperationExpression> calcualteFocusParentConfigs(){
			final List<OpperationExpression> configs = new ArrayList<OpperationExpression>();
			OpperationExpression parentCopy = null;
			final OpperationExpression parent = (OpperationExpression)MouseEventHandler._focus.getParent();
			final int focusIndex;

			// Make sure a parent expression exists
			if(parent != null){
				focusIndex = parent.getChildren().indexOf(MouseEventHandler._focus);

				// Generate the possible configurations and add them to a list.
				for(int i=0;i<parent.getChildren().size();i++){
					parentCopy = (OpperationExpression)parent.deepCopy();
					parentCopy.getChildren().add(i, parentCopy.getChildren().remove(focusIndex));
					configs.add(parentCopy);
				}
				return configs;
			}

			return null;
		}

		/**
			* Finds the node configuration that is closest to the mouse x coordinate.
			* @param x the x coordinate of the mouse.
			* @return the node configuration that is closest to the mouse.
			*/
		private static int findNearestConfigIndex(double x){
			final Map<Expression, Double> siblingSizes = new HashMap<Expression, Double>();
			final List<Double> pos = new ArrayList<Double>();
			final OpperationExpression parent = (OpperationExpression)MouseEventHandler._focus.getParent();
			System.out.println(MouseEventHandler._root.getNode().localToScene(MouseEventHandler._root.getNode().getBoundsInLocal()).getMinX());

			for(Expression e : parent.getChildren()){
				if(!MouseEventHandler._focus.equals(e)){
				siblingSizes.put(e, e.getNode().getLayoutBounds().getWidth());
				}
			}

			//have list of expresions
			for(OpperationExpression e  : MouseEventHandler._configs){
				double sumBeforeFocus = 0;
				for(Expression c : e.getChildren()){ // each config's children
					if(!c.equals(MouseEventHandler._focus)){
						sumBeforeFocus += siblingSizes.get(MouseEventHandler.findCorrectC(c, parent));
					}
					else{
						break;
					}
				}
				pos.add(MouseEventHandler._root.getNode().localToScene(MouseEventHandler._root.getNode().getBoundsInLocal()).getMinX() +
				sumBeforeFocus);
			}

			int closestIndex = 0;
			for(int i=1;i<pos.size();i++){
				if(Math.abs(pos.get(i) - x) < Math.abs(pos.get(closestIndex) - x)){
					closestIndex = i;
				}
			}

			for(double d : pos)
				System.out.println(d);
			return closestIndex;

		}

		/**
			* Find the original node from a copy.
			* @param c the copy that is to be found
			* @param parent the parent of the original expresion
			* @return the original that the copy was made from.
			*/
		private static Expression findCorrectC(Expression c, OpperationExpression parent){
			for(Expression e : parent.getChildren()){
				if(e.equals(c)){
					return e;
				}
			}

			return null;
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
