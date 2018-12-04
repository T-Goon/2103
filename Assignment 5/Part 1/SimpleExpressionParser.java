import java.lang.Character;

/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := A | X
 * A := A+M | M
 * M := M*M | X
 * X := (E) | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
	/**
	 * Attempts to create an expression tree -- flattened as much as possible -- from the specified String.
         * Throws a ExpressionParseException if the specified string cannot be parsed.
	 * @param str the string to parse into an expression tree
	 * @param withJavaFXControls you can just ignore this variable for R1
	 * @return the Expression object representing the parsed expression tree
	 */
	public Expression parse (String str, boolean withJavaFXControls) throws ExpressionParseException {
		// Remove spaces -- this simplifies the parsing logic
		str = str.replaceAll(" ", "");

		Expression expression = parseExpression(str);

		if (expression == null) {
			// If we couldn't parse the string, then raise an error
			throw new ExpressionParseException("Cannot parse expression: " + str);
		}

		// Flatten the expression before returning
		expression.flatten();
		return expression;
	}

	protected Expression parseExpression (String str) {
		Expression expression;

		// TODO implement me

		if(str.indexOf(0)equals("(")){
			expression =  new OpperationExpression("()", null);
			this.parseX(str.substring(1,str.length() - 1), expression);
		}

		return null;
	}

	/**
		* @return
		*/
	private Expression parseE(String str){

	}

	private Expression parseA(String str){

	}

	private Expression parseM(String str){

	}

	/**
		* Checks if the string starts and ends with a open/close paren.
		* @param str the string that is being parsed
		* @return an expression if the string follows the L procution rule and null
		* otherwise
		*/
	private static Expression parseX(String str){ // iffy on the (1 + 1) + (1 + 1) case should be fine though
		// Check if string is encased in parens.
		if(str.charAt(0).equals("(") && str.charAt(str.length()).equals(")")){
			final OpperationExpression exp = new OpperationExpression("()", null);
			// Check if inside the parens is a valid E
			final Expression eExpresssion = SimpleExpressionParser.parseE(str.substring(1, str.length()-1));

			if(eExpresssion == null)
				return null;

			// If inner string is a valid E then add it as a child of the () expression
			// and return
			exp.addSubexpression(eExpresssion);

			return exp;
		}

		return SimpleExpressionParser.parseL(str);
	}

	/**
		* Checks if the string is of size 1 and is either a digit 0-9 or a letter a-z
		* @param str the string that is being parsed
		* @return an expression if the string follows the L procution rule and null
		* otherwise
		*/
	private static Expression parseL(String str){
		if(str.length() == 1 && Character.isLetterOrDigit(str.charAt(0))){
				return new LiteralExpression(str, null);

		}
		return null;
	}

}
