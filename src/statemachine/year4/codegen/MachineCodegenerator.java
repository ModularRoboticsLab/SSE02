package statemachine.year4.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;
import statemachine.year3.dsl.FluentMachine;
import statemachine.year3.dsl.IntegerState;

public abstract class MachineCodegenerator extends FluentMachine implements ICodeGenerator {

	private StringBuilder builder;
	private Set<String> variables;
	private Map<String,Integer> eventMap;
	private Map<String,Integer> stateMap;
	private int eventIDcounter;
	private int stateIDcounter;
	
	@Override
	protected Transition createTransitionHook(String target, 
			Effect effect, IntegerState effectVar, int effectArg, 
			Condition cond, IntegerState condVariableMaybe, int condValue) {
		return new TransitionHolder(target,
                effect,effectVar,effectArg,
                cond, condVariableMaybe,condValue);
	}
	
	@Override
	public String generate(String packageName, String className) {
		builder = new StringBuilder();
		variables = new HashSet<String>();
		eventMap = new HashMap<String,Integer>();
		stateMap = new HashMap<String,Integer>();
		eventIDcounter = 0;
		stateIDcounter = 0;
		generateHeader(packageName, className);
		beginGenerateStates();
		for(State state: getAllStates())
			generateState(state);
		finishGenerateStates();
		generateVariableDeclarations();
		generateInitializer();
		generateFooter();
		return builder.toString();
	}

	private int getStateID(String name) {
		if(name==null) throw new Error("Illegal null name");
		if(stateMap.get(name)==null)
			stateMap.put(name, stateIDcounter++);
		return stateMap.get(name);
	}
	
	private int getEventID(String name) {
		if(name==null) throw new Error("Illegal null event");
		if(eventMap.get(name)==null)
			eventMap.put(name, eventIDcounter++);
		return eventMap.get(name);
	}
	
	private void _(String text) {
		builder.append(text);
		builder.append('\n');
	}
	
	private void generateHeader(String packageName, String className) {
		_("// Automatically generated code, do not edit");
		_("package "+packageName+";");
		_("import java.util.Map;");
		_("import statemachine.year4.codegen.GeneratedMachine;");
		_("public class "+className+" extends GeneratedMachine {");
	}
	
	private void beginGenerateStates() {
		_("  @Override protected void internalProcessEvent(int event) {");
		_("    switch(state) {");
	}
	
	private void generateState(State state) {
		_("    case "+getStateID(state.getName())+":");
		_("      switch(event) {");
		for(String event: state.getApplicableEvents()) {
			_("      case "+getEventID(event)+":");
			boolean first = true;
			for(Transition transition: state.getTransitionsForEvent(event)) {
				if(first)
					first = false;
				else
					_("      else");
				TransitionHolder holder = (TransitionHolder)transition;
				String indent = "        ";
				if(holder.getCond()!=null) {
					Condition c = holder.getCond();
					String operator;
					if(c==Condition.EQUAL) operator = "==";
					else if(c==Condition.GREATER) operator = ">";
					else throw new Error("Internal error");
					IntegerState var = holder.getCondVariableMaybe();
					variables.add(var.getName());
					_(indent+"if("+var.getName()+operator+holder.getCondValue()+") {");
				} 
				else 
					_(indent+"{");
				if(holder.getEffect()!=null) {
					Effect e = holder.getEffect();
					String operator;
					if(e==Effect.CHANGE) operator = "+=";
					else if(e==Effect.SET) operator = "=";
					else throw new Error("Internal error");
					IntegerState var = holder.getEffectVar();
					variables.add(var.getName());
					_(indent+"  "+var.getName()+operator+holder.getEffectArg()+";");
				}
				if(holder.getTarget()!=null) _(indent+"  state = "+getStateID(holder.getTarget())+";");
				_(indent+"}");
			}
			_("      break;");
		}
		_("      default: ; // ignore");
		_("      }");
		_("    break;");
	}

	private void finishGenerateStates() {
		_("    default: throw new Error(\"Internal error: unsupported state code\");");
		_("    }");
		_("  }");
	}

	private void generateVariableDeclarations() {
		for(String var: variables) {
			_("  private int "+var+";");
			_("  public int get_"+var+"() { return "+var+"; }");
		}
	}

	private void generateInitializer() {
		_("  @Override protected void internalInitialize(Map<String, Integer> event_code2int, Map<Integer, String> state_int2code) {");
		for(Map.Entry<String, Integer> state: stateMap.entrySet())
			_("    state_int2code.put("+state.getValue()+",\""+state.getKey()+"\");");
		for(Map.Entry<String, Integer> event: eventMap.entrySet())
			_("    event_code2int.put(\""+event.getKey()+"\","+event.getValue()+");");
		_("  }");
	}

	private void generateFooter() {
		_("}");
	}

}
