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
		* Checks if the string follows the E procution rules and returns a parsed
		* expression tree of the string.
		* @param str the string to be parsed
		* @return the pased expression tree of the string if it follows the procution
		* rule and null otherwise
		*/
	private Expression parseE(String str){
		Expression aExpression = SimpleExpressionParser.parseA(str);
		if(aExpression != null)
			return aExpression;

		Expression xExpression = SimpleExpressionParser.parseX(str);
		if(xExpression != null)
			return xExpression;
	}

	/**
		* Checks if the string follows the A procution rule and returns a parsed expression
		* tree of the string.
		* @param str the string to be parsed
		* @return the parsed expression tree if it follows the production rules and null
		* otherwise
		*/
	private static Expression parseA(String str){
		SimpleExpressionParser.parseMAHelper(str, "+", SimpleExpressionParser :: parseA,
		SimpleExpressionParser :: parseM, SimpleExpressionParser :: parseM);
	}

	/**
		* Checks if the string follows the M procution rule and returns a parsed expression
		* tree of the string.
		* @param str the string to be parsed
		* @return the parsed expression tree if it follows the production rules and null
		* otherwise
		*/
	private static Expression parseM(String str){
		SimpleExpressionParser.parseMAHelper(str, "*", SimpleExpressionParser :: parseM,
		SimpleExpressionParser :: parseM, SimpleExpressionParser :: parseX);
	}

	/**
		* Checks if the string follows the M or A production rule and returns a parsed
		* expression tree of the string.
		* @param str the string to be parsed
		* @param op the operator that the string's parsing is going to be based on
		* @param f1 the function that is used to check the expression before the op
		* @param f2 the function that is used to check the expression afther the op
		* @param f3 the function that is used to check the M/X cases of the procution rule
		* @return an expression tree of the string if it follows the production rule
		* and null otherwise
		*/
	private static Expression parseMAHelper(String str, char op,
	Function<String, Expression> f1, Function<String, Expression> f2,
	Function<String, Expression> f3){
		for (int i = 1; i < str.length() - 1; i++) {

			if (str.charAt(i) == op){
				final Expression beforeOp = f1(str.substring(0, i));
				final Expression afterOp = f2(str.substring(i+1));

				if(beforeOp != null && afterOp != null) {
					final OpperationExpression exp = new OpperationExpression(op, null);
					exp.addSubexpression(beforeOp);
					exp.addSubexpression(afterOp;
					return exp;
				}

			}

		}

		return f3(str);

	}

	/**
		* Checks if the string follows the X procution rule and returns a parsed
		* expression tree of the string.
		* @param str the string that is being parsed
		* @return an expression tree of the string follows the L procution rule and null
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
		* Checks if the string follows the L procution rule and returns a parsed
		* expression tree of the string.
		* @param str the string that is being parsed
		* @return an expression tree of the string follows the L procution rule and null
		* otherwise
		*/
	private static Expression parseL(String str){
		// checks if string is size 1 and a digit/letter
		if(str.length() == 1 && Character.isLetterOrDigit(str.charAt(0))){
				return new LiteralExpression(str, null);

		}

		return null;
	}

}
