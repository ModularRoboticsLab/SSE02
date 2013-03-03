package statemachine.year5.compiler;

/**
 * Signal a parse error
 * @author ups
 *
 */
public class ParseError extends Exception {

	private static final long serialVersionUID = -8529799273195644375L;
	
	/**
	 * Create a parse error with the given message
	 * @param message the error message
	 */
	public ParseError(String message) {
		super(message);
	}

}
