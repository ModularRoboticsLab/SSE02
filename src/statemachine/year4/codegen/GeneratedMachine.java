package statemachine.year4.codegen;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import statemachine.year1.library.Event;
import statemachine.year1.library.IMachine;

/**
 * Abstract class that serves as the basis for code generated for statemachines.
 * Hooks up the regular String-based event and state approach to one based on integer IDs
 * @author ups
 *
 */
public abstract class GeneratedMachine extends Observable implements IMachine {

	/**
	 * The current state of the state machine, represented as an integer
	 */
	protected int state;
	/**
	 * Map from event names to event IDs
	 */
	private Map<String,Integer> event_code2int = new HashMap<String,Integer>();
	/**
	 * Map from state IDs to state names
	 */
	private Map<Integer,String> state_int2code = new HashMap<Integer,String>();
	
	/**
	 * Initialize the state machine, including the event and state maps
	 */
	@Override
	public void initialize() {
		internalInitialize(event_code2int, state_int2code);
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Process an event with the corresponding String-based event code
	 * @param event the event to process
	 */
	@Override
	public void processEvent(Event event) {
		internalProcessEvent(event_code2int.get(event.code()));
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Get the name of the currently active state
	 * @return the current state
	 */
	@Override
	public String getStateName() {
		return state_int2code.get(state);
	}

	/**
	 * Template method for the compiled event processed based on integer state IDs
	 * @param code
	 */
	protected abstract void internalProcessEvent(int code);

	/**
	 * Template method for initializing the maps between state and event names and their corresponding IDs
	 * @param event_code2int Map from event name to event code ID
	 * @param state_int2code Map from state ID to state name
	 */
	protected abstract void internalInitialize(Map<String, Integer> event_code2int, Map<Integer, String> state_int2code);

}
