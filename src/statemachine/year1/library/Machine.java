package statemachine.year1.library;

import java.util.Observable;

public abstract class Machine extends Observable {
    
    private State currentState;
    
    public void initialize() {
        setState(getInitialState());
        setChanged();
        notifyObservers();
    }
    
    public void setState(State state) {
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
    
    protected abstract State getInitialState();

}
