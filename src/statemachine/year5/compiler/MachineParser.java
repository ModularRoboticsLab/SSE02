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
		lex.popAndMatchToken("machine");
		name = lex.popToken("machine name");
	}
	
	private boolean parseVariableName(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		if(!lex.checkMatch("variable")) return false;
		lex.popAndMatchToken("variable");
		makeExternalVariable(lex.popToken("variable name"));
		return true;
	}
	
	private boolean parseStateLine(String line) throws ParseError {
		Lexer lex = new Lexer(line);
		if(!lex.checkMatch("state")) return false;
		lex.popAndMatchToken("state");
		super.state(lex.popToken("state"));
		return true;
	}
	
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

	private IntegerState getExtendedVariable(String name) throws ParseError {
		IntegerState state = externalVariables.get(name);
		if(state==null) throw new ParseError("Variable not found: "+name);
		return state;
	}

	private void makeExternalVariable(String name) {
		externalVariables.put(name, new IntegerState(name));
	}

}
