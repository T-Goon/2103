import java.lang.Character;
import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;
import java.lang.NumberFormatException;

/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := A | X
 * A := A+M | M
 * M := M*M | X
 * X := (E) | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
	private static final List<Function<String, Expression>> LISTOFPARSERS = new ArrayList<Function<String, Expression>>()
																																	{{add(SimpleExpressionParser::parseE);
																																		add(SimpleExpressionParser :: parseX);
																																		add(SimpleExpressionParser :: parseA);
																																		add(SimpleExpressionParser :: parseM);
																																		add(SimpleExpressionParser :: parseL);}};
	//private static final List<Function<String, Expression>> LISTOFPARSERS = new ArrayList<Function<String, Expression>>(){SimpleExpressionParser::parseX};

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

	/**
		* Parse the string into a parse tree if it follows the procution rules.
		* @param str the string to be parsed
		* @return the expression tree if it follows the production rules and
		* null otherwise
		*/
	protected Expression parseExpression (String str) {
		Expression expression;

		// Tests if the string follows any of the production rules
		for(Function<String, Expression> f : SimpleExpressionParser.LISTOFPARSERS){
			expression = f.apply(str);
			// if string does follow a production rule then return the parsed tree
			if(expression != null){
				return expression;
			}
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
	private static Expression parseE(String str){
		// Checks if string follows the A production rule
		final Expression aExpression = SimpleExpressionParser.parseA(str);
		if(aExpression != null)
			return aExpression;

		// Checks if string follows the X production rule
		final Expression xExpression = SimpleExpressionParser.parseX(str);
		if(xExpression != null)
			return xExpression;

		return null;
	}

	/**
		* Checks if the string follows the A procution rule and returns a parsed expression
		* tree of the string.
		* @param str the string to be parsed
		* @return the parsed expression tree if it follows the production rules and null
		* otherwise
		*/
	private static Expression parseA(String str){
		return SimpleExpressionParser.parseMAHelper(str, '+', SimpleExpressionParser :: parseA,
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
		return SimpleExpressionParser.parseMAHelper(str, '*', SimpleExpressionParser :: parseM,
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
		// Iterates through the string and looks for the passed in opperator
		for (int i = 1; i < str.length() - 1; i++) {

			if (str.charAt(i) == op){
				// If it has found the opperator then check if the strings to the left
				// and right of the opperator follow the passed in production rules
				final Expression beforeOp = f1.apply(str.substring(0, i));
				final Expression afterOp = f2.apply(str.substring(i+1));

				if(beforeOp != null && afterOp != null) {
					final OpperationExpression exp = new OpperationExpression(op+"");
					exp.addSubexpression(beforeOp);
					exp.addSubexpression(afterOp);

					return exp;
				}

			}

		}

		// Check if the string follows the third passed in production rule
		return f3.apply(str);

	}

	/**
		* Checks if the string follows the X procution rule and returns a parsed
		* expression tree of the string.
		* @param str the string that is being parsed
		* @return an expression tree of the string follows the L procution rule and null
		* otherwise
		*/
	private static Expression parseX(String str){
		// Check if string is encased in parens. and that it is greater than size 1
		if(str.length() > 0 && str.charAt(0) == '(' && str.charAt(str.length()-1) == ')'){
			final OpperationExpression exp = new OpperationExpression("()");
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
		// checks if string is size 1 and a letter
		if(str.length() == 1 && Character.isLetter(str.charAt(0))){
				return new LiteralExpression(str);

		}

		// Checks if the string is a positive integer
		try{
			if(Integer.parseInt(str) >= 0 && str.charAt(0) != '+' && str.charAt(0) != '-'){
				return new LiteralExpression(str);
			}
		}
		catch(NumberFormatException e){
			return null;
		}

		return null;
	}

}
