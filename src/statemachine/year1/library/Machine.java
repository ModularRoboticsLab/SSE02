package statemachine.year1.library;

import java.util.Observable;

/**
 * Abstract state machine class: has a current state, starts in initial state,
 * and can process events by sending them to the current state
 * @author ups
 */
public abstract class Machine extends Observable implements IMachine {
    
    private State currentState;
    
    public void initialize() {
        setState(getInitialState());
        setChanged();
        notifyObservers();
    }
    
    public void setState(State state) {
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
    
    protected abstract State getInitialState();

}
