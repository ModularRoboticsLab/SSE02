package statemachine.year4.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;
import statemachine.year3.dsl.FluentMachine;
import statemachine.year3.dsl.FluentMachine.Condition;
import statemachine.year3.dsl.FluentMachine.Effect;
import statemachine.year3.dsl.IntegerState;

/**
 * Code generator for state machines described using the FluentMachine model.
 * Generates a single class definition that subclasses GeneratedMachine and
 * implements the two methods for processing events and for initializing the
 * maps between names and integer IDs.  Moreover, also generates fields and
 * getter methods for extended state.
 * @author ups
 *
 */
public class MachineJavaCodeGenerator {

	/**
	 * The model according to which the code is generated
	 */
	private FluentMachine model;
	/**
	 * The buffer in which the output string (the classs definition) is accumulated
	 */
	private StringBuilder builder;
	/**
	 * The set of extended state variables used in the state machine.  Accumulates during
	 * generation and used to create definitions and accessor methods at the end
	 */
	private Set<String> variables;
	/**
	 * Map from event names to their corresponding integer ID
	 */
	private Map<String,Integer> eventMap;
	/**
	 * Map from state names to their corresponding integer ID
	 */
	private Map<String,Integer> stateMap;
	/**
	 * Counter for assigning fresh IDs to event names
	 */
	private int eventIDcounter;
	/**
	 * Counter for assigned fresh IDs to state names
	 */
	private int stateIDcounter;
	
	/**
	 * Create a code generator for the corresponding state machine model
	 * @param model the state machine for which code is to be generated
	 */
	public MachineJavaCodeGenerator(FluentMachine model) {
		this.model = model;
	}

	/**
	 * Generate code for the corresponding state machine, named according to the supplied class
	 * name and package name. 
	 * @param packageName the package name to use for the generated class definition
	 * @param className the class name to use for the generated class definition
	 * @return the text of a complete class definition
	 */
	public String generate(String packageName, String className) {
		builder = new StringBuilder();
		variables = new HashSet<String>();
		eventMap = new HashMap<String,Integer>();
		stateMap = new HashMap<String,Integer>();
		eventIDcounter = 0;
		stateIDcounter = 0;
		generateHeader(packageName, className);
		beginGenerateStates();
		for(State state: model.getAllStates())
			generateState(state);
		finishGenerateStates();
		generateVariableDeclarations();
		generateInitializer();
		generateFooter();
		return builder.toString();
	}

	/**
	 * Map a state name to an integer ID, creates new IDs on-demand
	 * @param name name of a state of the state machine
	 * @return a unique ID corresponding to that exact state name
	 */
	private int getStateID(String name) {
		if(name==null) throw new Error("Illegal null name");
		if(stateMap.get(name)==null)
			stateMap.put(name, stateIDcounter++);
		return stateMap.get(name);
	}
	
	/**
	 * Map an event name to an integer ID, creates new IDs on-demand
	 * @param name name of an event that the state machine can respond to
	 * @return a unique ID corresponding to that exact event name
	 */
	private int getEventID(String name) {
		if(name==null) throw new Error("Illegal null event");
		if(eventMap.get(name)==null)
			eventMap.put(name, eventIDcounter++);
		return eventMap.get(name);
	}
	
	/**
	 * Convenience method for appending text to the accumulating buffer and then adding a newline
	 * @param text the text to append to the buffer
	 */
	private void _(String text) {
		builder.append(text);
		builder.append('\n');
	}
	
	/**
	 * Generate the class header: package, import, and class declaration
	 * @param packageName the package name to use
	 * @param className the class name to use
	 */
	private void generateHeader(String packageName, String className) {
		_("// Automatically generated code, do not edit");
		_("package "+packageName+";");
		_("import java.util.Map;");
		_("import statemachine.year4.codegen.GeneratedMachine;");
		_("public class "+className+" extends GeneratedMachine {");
	}
	
	/**
	 * Generate the method header and initial code for the state processing method
	 */
	private void beginGenerateStates() {
		_("  @Override protected void internalProcessEvent(int event) {");
		_("    switch(state) {");
	}
	
	/**
	 * Generate the code for handling a single state
	 * @param state the state to handle
	 */
	private void generateState(State state) {
		// CASE STATE:
		_("    case "+getStateID(state.getName())+": // "+state.getName());
		_("      switch(event) {");
		for(String event: state.getApplicableEvents()) {
			// CASE EVENT:
			_("      case "+getEventID(event)+": // "+event);
			boolean first = true;
			for(Transition transition: state.getTransitionsForEvent(event)) {
				// IF(TRANSITION IS APPLICABLE)
				if(first)
					first = false;
				else
					_("      else");
				TransitionHolder holder = (TransitionHolder)transition;
				String indent = "        ";
				if(holder.getCond()!=null) {
					// What kind of condition?
					Condition c = holder.getCond();
					String operator;
					if(c==Condition.EQUAL) operator = "==";
					else if(c==Condition.GREATER) operator = ">";
					else throw new Error("Internal error");
					// Generate condition
					IntegerState var = holder.getCondVariableMaybe();
					variables.add(var.getName());
					_(indent+"if("+var.getName()+operator+holder.getCondValue()+") {");
				} 
				else 
					_(indent+"{");
				// TRANSITION EFFECT
				if(holder.getEffect()!=null) {
					// What kind of effect?
					Effect e = holder.getEffect();
					String operator;
					if(e==Effect.CHANGE) operator = "+=";
					else if(e==Effect.SET) operator = "=";
					else throw new Error("Internal error");
					// Generate effect
					IntegerState var = holder.getEffectVar();
					variables.add(var.getName());
					_(indent+"  "+var.getName()+operator+holder.getEffectArg()+";");
				}
				// TRANSITION CHANGE STATE
				if(holder.getTarget()!=null) _(indent+"  state = "+getStateID(holder.getTarget())+"; // "+holder.getTarget());
				_(indent+"}");
			}
			_("      break;");
		}
		_("      default: ; // ignore");
		_("      }");
		_("    break;");
	}

	/**
	 * Generate the end of the event processing method
	 */
	private void finishGenerateStates() {
		_("    default: throw new Error(\"Internal error: unsupported state code\");");
		_("    }");
		_("  }");
	}

	/**
	 * Generate declarations and getter methods for extended state
	 */
	private void generateVariableDeclarations() {
		for(String var: variables) {
			_("  private int "+var+";");
			_("  /** Get the value of the extended state "+var);
			_("    * @return value of "+var);
			_("  */");
			_("  public int get_"+var+"() { return "+var+"; }");
		}
	}

	/**
	 * Generate initializer method for establishing maps between IDs and corresponding names
	 */
	private void generateInitializer() {
		_("  @Override protected void internalInitialize(Map<String, Integer> event_code2int, Map<Integer, String> state_int2code) {");
		for(Map.Entry<String, Integer> state: stateMap.entrySet())
			_("    state_int2code.put("+state.getValue()+",\""+state.getKey()+"\");");
		for(Map.Entry<String, Integer> event: eventMap.entrySet())
			_("    event_code2int.put(\""+event.getKey()+"\","+event.getValue()+");");
		_("  }");
	}

	/**
	 * Generate end of the class declaration
	 */
	private void generateFooter() {
		_("}");
	}

}
