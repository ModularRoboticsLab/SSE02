package statemachine.year4.codegen;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import statemachine.year1.library.Event;
import statemachine.year1.library.IMachine;

public abstract class GeneratedMachine extends Observable implements IMachine {

	protected int state;
	private Map<String,Integer> event_code2int = new HashMap<String,Integer>();
	private Map<Integer,String> state_int2code = new HashMap<Integer,String>();
	
	@Override
	public void initialize() {
		internalInitialize(event_code2int, state_int2code);
		System.out.println("Machine initialized");
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void processEvent(Event event) {
		internalProcessEvent(event_code2int.get(event.code()));
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public String getStateName() {
		return state_int2code.get(state);
	}

	protected abstract void internalProcessEvent(int code);

	protected abstract void internalInitialize(Map<String, Integer> event_code2int2, Map<Integer, String> state_int2code2);

}
