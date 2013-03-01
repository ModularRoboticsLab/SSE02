package statemachine.year5.compiler;

import java.util.StringTokenizer;

public class Lexer {

	private StringTokenizer tokenizer;
	private String next;
	
	private String nextToken() {
		if(!tokenizer.hasMoreTokens()) return null;
		if(next==null) next = tokenizer.nextToken();
		return next;
	}
	
	public Lexer(String line) {
		tokenizer = new StringTokenizer(line);
	}

	public boolean match(String string) {
		String n = nextToken();
		if(n==null) return false;
		return n.equals(string);
	}

	public String next(String kind) throws ParseError {
		String n = nextToken();
		if(n==null) throw new ParseError("Expected token "+kind+", but end of line");
		next = null;
		return n;
	}

	public void nextMatch(String string) throws ParseError {
		String n = nextToken();
		if(n==null) throw new ParseError("Expected token "+string+", but end of line");
		if(!n.equals(string)) throw new ParseError("Expected token "+string+", but got "+n);
		next = null;
	}

	public Integer nextInt() throws ParseError {
		String n = nextToken();
		if(n==null) throw new ParseError("Expected number token, but end of line");
		Integer i = Integer.parseInt(n);
		if(i==null) throw new ParseError("Expected number token, but got "+i);
		next = null;
		return i;
	}

}
