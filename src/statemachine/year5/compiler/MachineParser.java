package statemachine.year5.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import statemachine.year3.dsl.FluentMachine;
import statemachine.year3.dsl.IntegerState;

/**
 * Parse a source file and use it to build a semantic model using the existing fluent machine interface.
 * @author ups
 *
 */
public class MachineParser extends FluentMachine {

	/**
	 * Map of external variables
	 */
	private Map<String,IntegerState> externalVariables = new HashMap<String,IntegerState>();
	/**
	 * The input text to parse
	 */
	private BufferedReader stream;
	/**
	 * The full name of the machine
	 */
	private String name;
	
	/**
	 * Create a parser for the given input stream
	 * @param stream the input to parse
	 */
	public MachineParser(BufferedReader stream) {
		this.stream = stream;
	}
	
	/**
	 * Get the full name of the machine
	 * @return the machine name
	 */
	public String getMachineName() {
		super.buildMachine();
		return name;
	}
	
	/**
	 * Parse the input text, building the semantic model
	 */
	@Override
	protected void build() {
		String line = "EOF";
		try {
			// First line: name
			line = stream.readLine();
			if(line==null) throw new ParseError("Empty file");
			parseStateMachineName(line);
			// Read variable declarations
			do {
				line = stream.readLine();		
			} while(line!=null && parseVariableName(line));
			// Read each state declaration
			while(line!=null && parseStateLine(line)) {
				// Read all transitions of the current state
				do {
					line = stream.readLine();
				} while(line!=null && parseTransitionLine(line));
			}
		} catch(IOException exn) {
			throw new Error("I/O exception reading file: "+exn+" at line "+line);
		} catch(ParseError err) {
			System.err.println("Error parsing file: "+err+" at line "+line);
			throw new Error(err);
		}
	}
	
	/**
	 * Parse the statemachine name declaration
	 * @param line the line to parse
	 * @throws ParseError if the line does not define the name
	 */
	private void parseStateMachineName(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		lex.popAndMatchToken("machine");
		name = lex.popToken("machine name");
	}
	
	/**
	 * Attempt to parse a single line as a variable name declaration
	 * @param line the line to parse
	 * @return true if the line declared a variable, false otherwise
	 * @throws ParseError if the line contained a partial (incorrect) variable declaration
	 */
	private boolean parseVariableName(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		if(!lex.checkMatch("variable")) return false;
		lex.popAndMatchToken("variable");
		makeExternalVariable(lex.popToken("variable name"));
		return true;
	}
	
	/**
	 * Parse the declaration of a state name
	 * @param line the line to parse
	 * @return true if this line declares a state name, false otherwise
	 * @throws ParseError if the line contained a partial (incorrect) state name declaration
	 */
	private boolean parseStateLine(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		if(!lex.checkMatch("state")) return false;
		lex.popAndMatchToken("state");
		super.state(lex.popToken("state"));
		return true;
	}
	
	/**
	 * Parse a single state transition line.  "Else" clauses currently do not support actions.
	 * @param line the line to parse
	 * @return true if the line declared a state transition that was matched, false otherwise
	 * @throws ParseError if the line was recognized as a state transition but had bad syntax
	 */
	private boolean parseTransitionLine(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		if(lex.checkMatch("state")) return false;
		// Else line?
		if(lex.checkMatch("else")) {
			lex.popAndMatchToken("else");
			String state = lex.popToken("state");
			super.to(state);
			super.otherwise();
			// Skip any action
			return true;
		}
		// Event
		String event = lex.popToken("event");
		super.transition(event);
		// Condition?
		IntegerState condVariable = null;
		String condOperator = null;
		Integer condValue = null;
		if(lex.checkMatch("[")) {
			lex.popAndMatchToken("[");
			 condVariable = getExtendedVariable(lex.popToken("variable"));
			 condOperator = lex.popToken("operator");
			 condValue = lex.popTokenAsInt();
			 lex.popAndMatchToken("]");
		}
		// Target state
		lex.popAndMatchToken("to");
		super.to(lex.popToken("state"));
		// Action?
		if(lex.checkMatch("/")) {
			lex.popAndMatchToken("/");
			IntegerState actionVariable = getExtendedVariable(lex.popToken("variable"));
			String actionOperator = lex.popToken("oeprator");
			Integer actionValue = lex.popTokenAsInt();
			if("=".equals(actionOperator))
				super.setState(actionVariable, actionValue);
			else if("+".equals(actionOperator))
				super.changeState(actionVariable, actionValue);
			else
				throw new ParseError("Illegal action operator: "+actionOperator);
		}
		// Insert condition, if any
		if("=".equals(condOperator))
			super.whenStateEquals(condVariable, condValue);
		else if(">".equals(condOperator))
			super.whenStateGreaterThan(condVariable, condValue);
		else if(condOperator!=null)
			throw new ParseError("Illegal condition operator: "+condOperator);
		return true;
	}

	/**
	 * Get the extended variable of the given name
	 * @param name the name of the extended variable
	 * @return the extended variable of the corresponding name
	 * @throws ParseError if the name was not found
	 */
	private IntegerState getExtendedVariable(String name) throws ParseError {
		IntegerState state = externalVariables.get(name);
		if(state==null) throw new ParseError("Variable not found: "+name);
		return state;
	}

	/**
	 * Create an external variable, and store it for later retrieval
	 * @param name the name of the external variable
	 */
	private void makeExternalVariable(String name) {
		externalVariables.put(name, new IntegerState(name));
	}

}
