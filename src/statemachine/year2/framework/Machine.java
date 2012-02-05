package statemachine.year2.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import statemachine.year1.library.Event;

public abstract class Machine extends Observable {
    
    private State currentState;
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

    public State getState() {
        return currentState;
    }

    
    public void processEvent(Event event) {
        if(currentState==null) throw new Error("State machine not initialized");
        currentState.processEvent(event);
        setChanged();
        notifyObservers();
    }
    
    protected abstract List<State> getAllStates();

}
