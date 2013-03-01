package statemachine.year5.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import statemachine.year3.dsl.FluentMachine;
import statemachine.year3.dsl.IntegerState;

public class MachineParser extends FluentMachine {

	private Map<String,IntegerState> externalVariables = new HashMap<String,IntegerState>();
	private BufferedReader stream;
	private String name;
	
	public MachineParser(BufferedReader stream) {
		this.stream = stream;
	}
	
	public String getMachineName() {
		super.buildMachine();
		return name;
	}
	
	@Override
	protected void build() {
		String line = "EOF";
		try {
			line = stream.readLine();
			if(line==null) throw new ParseError("Empty file");
			parseStateMachineName(line);
			do {
				line = stream.readLine();		
			} while(line!=null && parseVariableName(line));
			while(line!=null && parseStateLine(line)) {
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
	
	private void parseStateMachineName(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		lex.nextMatch("machine");
		name = lex.next("machine name");
	}
	
	private boolean parseVariableName(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		if(!lex.match("variable")) return false;
		lex.nextMatch("variable");
		makeExternalVariable(lex.next("variable name"));
		return true;
	}
	
	private boolean parseStateLine(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		if(!lex.match("state")) return false;
		lex.nextMatch("state");
		super.state(lex.next("state"));
		return true;
	}
	
	private boolean parseTransitionLine(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		if(lex.match("state")) return false;
		// Event
		String event = lex.next("event");
		super.transition(event);
		// Condition?
		IntegerState variable = null;
		String operator = null;
		Integer value = null;
		if(lex.match("[")) {
			lex.nextMatch("[");
			 variable = getExtendedVariable(lex.next("variable"));
			 operator = lex.next("operator");
			 value = lex.nextInt();
			 lex.nextMatch("]");
		}
		// Target state
		lex.nextMatch("to");
		super.to(lex.next("state"));
		// Insert condition
		if("=".equals(operator))
			super.whenStateEquals(variable, value);
		else if(">".equals(operator))
			super.whenStateGreaterThan(variable, value);
		else if(operator!=null)
			throw new ParseError("Illegal condition operator: "+operator);
		// Action?
		if(lex.match("/")) {
			lex.nextMatch("/");
			variable = getExtendedVariable(lex.next("variable"));
			operator = lex.next("oeprator");
			value = lex.nextInt();
			if("=".equals(operator))
				super.setState(variable, value);
			else if("+".equals(operator))
				super.changeState(variable, value);
			else
				throw new ParseError("Illegal action operator: "+operator);
		}
		return true;
	}

	private IntegerState getExtendedVariable(String name) throws ParseError {
		IntegerState state = externalVariables.get(name);
		if(state==null) throw new ParseError("Variable not found: "+name);
		return state;
	}

	private void makeExternalVariable(String name) {
		externalVariables.put(name, new IntegerState(name));
	}

}
