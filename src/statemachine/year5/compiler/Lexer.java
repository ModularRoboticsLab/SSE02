package statemachine.year5.compiler;

import java.util.StringTokenizer;

/**
 * Simple lexer based on standard tokenizer, allows matching without removing tokens
 * from the stream. Operates on a single line at a time.
 * @author ups
 *
 */
public class Lexer {

	/**
	 * Tokenizer for breaking the line into words
	 */
	private StringTokenizer tokenizer;
	
	/**
	 * The current token, if any, null otherwise
	 */
	private String current;

	/**
	 * Create lexer for the given line
	 * @param line the line to be lexed
	 */
	public Lexer(String line) {
		tokenizer = new StringTokenizer(line);
	}

	/**
	 * Get the current token, either the stored one, or the first available one from token stream
	 * @return the current token, or null if none
	 */
	private String getCurrentToken() {
		if(!tokenizer.hasMoreTokens()) return null;
		if(current==null) current = tokenizer.nextToken();
		return current;
	}
	
	/**
	 * Check if the current token matches the given string
	 * @param expected the string to compare to the current token
	 * @return true if is matches, false otherwise
	 */
	public boolean checkMatch(String expected) {
		String n = getCurrentToken();
		if(n==null) return false;
		return n.equals(expected);
	}

	/**
	 * Removes the current token, generating an error if there are no tokens
	 * @param expectedKind the expected kind of token, used to signal an error
	 * @return the current token
	 * @throws ParseError if there are no more token available
	 */
	public String popToken(String expectedKind) throws ParseError {
		String n = getCurrentToken();
		if(n==null) throw new ParseError("Expected token "+expectedKind+", but end of line");
		current = null;
		return n;
	}

	/**
	 * Removes the current token, checking that it matches the provided text, signalling an error otherwise
	 * @param expected the text that the current token must match
	 * @throws ParseError if the current token does not match the expected text
	 */
	public void popAndMatchToken(String expected) throws ParseError {
		String n = getCurrentToken();
		if(n==null) throw new ParseError("Expected token "+expected+", but end of line");
		if(!n.equals(expected)) throw new ParseError("Expected token "+expected+", but got "+n);
		current = null;
	}

	/**
	 * Removes the current token, and converts it to an integer
	 * @return the integer value of the token
	 * @throws ParseError if there was no token or if the token was not a number
	 */
	public Integer popTokenAsInt() throws ParseError {
		String n = getCurrentToken();
		if(n==null) throw new ParseError("Expected number token, but end of line");
		Integer i = Integer.parseInt(n);
		if(i==null) throw new ParseError("Expected number token, but got "+i);
		current = null;
		return i;
	}

}
