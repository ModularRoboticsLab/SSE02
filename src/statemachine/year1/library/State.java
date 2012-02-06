package statemachine.year1.library;

/**
 * Abstract state class: can process events (subclasses must provide behavior),
 * keeps a reference to the machine to which it belongs to enable changing its state.
 * @author ups
 */
public abstract class State {

    private IMachine machine;

    public State(IMachine machine) {
        this.machine = machine;
    }

    public abstract void processEvent(Event event);
    
    public IMachine machine() {
        return machine;
    }

    public String toString() {
        String name = this.getClass().getSimpleName();
        if(name==null) name = this.getClass().getName();
        return name;
    }
}
