package statemachine.year1.library;

public abstract class State {

    private Machine machine;

    public State(Machine machine) {
        this.machine = machine;
    }

    public abstract void processEvent(Event event);
    
    public Machine machine() {
        return machine;
    }

    public String toString() {
        String name = this.getClass().getSimpleName();
        if(name==null) name = this.getClass().getName();
        return name;
    }
}
