import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class OpperationExpression implements CompoundExpression{
  private final String _opperation;
  private CompoundExpression _parent;
  private final List<Expression> _children;

  public OpperationExpression(String opperation){
    this._opperation = opperation;
    this._parent = _parent;
    this._children = new ArrayList<Expression>();
  }

  /**
	 * Adds the specified expression as a child.
	 * @param subexpression the child expression to add
	 */
	public void addSubexpression (Expression subexpression){
    this._children.add(subexpression);
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
    final OpperationExpression copy = new OpperationExpression(this._opperation);
    copy.setParent(this._parent);

    // Copy expression's subexpressions
    for(Expression exp : this._children){
      copy.addSubexpression(exp.deepCopy());
    }

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
    // Find the other opperation that this expression is not
    final String otherOpp;

    if(this._opperation.equals("+")){
      otherOpp = "*";
    }
    else{
      otherOpp = "+";
    }

    // A list of child nodes that are of the same opperation a this expression
    final List<OpperationExpression> toRemove = new ArrayList<OpperationExpression>();
    // Index of where the toRemove child nodes where in the original childs list
    final List<Integer> toRemoveIndexes = new ArrayList<Integer>();

    // Find subexpressions that have the same opperation as this expression and
    // add it to the list toRemove
    for(int i=0;i<this._children.size();i++){
      if(!this._children.get(i).toString().equals("()") && this._opperation.equals(this._children.get(i).toString())){
        toRemove.add((OpperationExpression)this._children.get(i));
        toRemoveIndexes.add(i);
      }
      // If the child is a paran expression or an expression of the other opperation type
      // flatten it.
      else if(this._children.get(i).toString().equals(otherOpp) || this._children.get(i).toString().equals("()")){
        this._children.get(i).flatten();
      }

    }

    // Add the children of the expressions that have the same opperation as this one
    // to this expression's children
    for(int i=0;i<toRemove.size();i++){
      this._children.addAll(toRemoveIndexes.get(i), toRemove.get(i).getChildren());
    }

    // Remove the subexpressions that have the same opperation as this one
    this._children.removeAll(toRemove);

    // Stop the recursion, nothing to flatten
    if(toRemove.isEmpty()){
      return;
    }

    // Flatten this expression again
    this.flatten();
  }

  /**
   * Creates a String representation by recursively printing out (using indentation) the
   * tree represented by this expression, starting at the specified indentation level.
   * @param stringBuilder the StringBuilder to use for building the String representation
   * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
   */
  public void convertToString (StringBuilder stringBuilder, int indentLevel){
    Expression.indent(stringBuilder, indentLevel);

    stringBuilder.append(this._opperation);
    stringBuilder.append("\n");

    for(Expression exp : this._children){
      exp.convertToString(stringBuilder, indentLevel + 1);
    }
  }

  /**
    * Returns the opperation of the opperation expression.
    * @return opperation of this expression
    */
  public String toString(){
    return this._opperation;
  }

  /**
    * @return this expression's subexpressions
    */
  public List getChildren(){
    return this._children;
  }

}
