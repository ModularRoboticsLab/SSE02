package statemachine.year2.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import statemachine.year1.library.Event;
import statemachine.year1.library.IMachine;

/**
 * State machine: keeps track of current state, stores a map from state name
 * to state object (used to perform state transitions).
 * @author ups
 */
public abstract class Machine extends Observable implements IMachine {

    /**
     * Current state
     */
    private State currentState;
    /**
     * Map from state name to state object
     */
    private Map<String,State> states = new HashMap<String,State>();
    
    public void initialize() {
        List<State> allStates = getAllStates();
        for(State state: allStates)
            states.put(state.getName(), state);
        setState(allStates.get(0).getName());
        setChanged();
        notifyObservers();
    }
    
    public void setState(String stateid) {
        State state = states.get(stateid);
        if(state==null) throw new Error("Illegal state identifier: "+stateid);
        currentState = state;
    }

    public String getStateName() {
        return currentState.toString();
    }

    
    public void processEvent(Event event) {
        if(currentState==null) throw new Error("State machine not initialized");
        currentState.processEvent(event);
        setChanged();
        notifyObservers();
    }
    
    /**
     * Overridden by concrete state machine.  By convention the first element must be
     * the initial state.
     * @return List of all states, first element is initial state
     */
    protected abstract List<State> getAllStates();

}
