import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

public class LiteralExpression implements Expression{
  private final String _value;
  private CompoundExpression _parent;
  private Node _box;

  public LiteralExpression(String value){
    this._value = value;
    this._parent = null;
    this._box = new HBox(new Label(this._value));
  }

  /**
   * Returns the expression's parent.
   * @return the expression's parent
   */
  public CompoundExpression getParent (){
    return this._parent;
  }

  /**
   * Sets the parent be the specified expression.
   * @param parent the CompoundExpression that should be the parent of the target object
   */
  public void setParent (CompoundExpression parent){
    this._parent = parent;
  }

  /**
   * Creates and returns a deep copy of the expression.
   * The entire tree rooted at the target node is copied, i.e.,
   * the copied Expression is as deep as possible.
   * @return the deep copy
   */
  public Expression deepCopy (){
    final LiteralExpression copy = new LiteralExpression(this._value);
    copy.setParent(this._parent);
    return copy;
  }

  /**
   * Recursively flattens the expression as much as possible
   * throughout the entire tree. Specifically, in every multiplicative
   * or additive expression x whose first or last
   * child c is of the same type as x, the children of c will be added to x, and
   * c itself will be removed. This method modifies the expression itself.
   */
  public void flatten (){
    // LiteralExpression is always flat so do nothing
  }

  /**
   * Creates a String representation by recursively printing out (using indentation) the
   * tree represented by this expression, starting at the specified indentation level.
   * @param stringBuilder the StringBuilder to use for building the String representation
   * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
   */
  public void convertToString (StringBuilder stringBuilder, int indentLevel){
    Expression.indent(stringBuilder, indentLevel);

    stringBuilder.append(this._value);
    stringBuilder.append("\n");
  }

  /**
    * Returns the value of the literal expression.
    * @return value of this expression
    */
  public String toString(){
    return this._value;
  }

  /**
   * Returns the JavaFX node associated with this expression.
   * @return the JavaFX node associated with this expression.
   */
  public Node getNode (){
    return new Label(this._value);
  }

}
