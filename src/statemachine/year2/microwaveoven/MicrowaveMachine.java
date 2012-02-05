package statemachine.year2.microwaveoven;

import java.util.Arrays;
import java.util.List;

import statemachine.year2.framework.Machine;
import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;

public class MicrowaveMachine extends Machine {

    private State STATE_INACTIVE, STATE_COOKING, STATE_DOOR_OPEN;
    
    public MicrowaveMachine() {
        STATE_INACTIVE = new State(this,"INACTIVE");
        STATE_INACTIVE.addTransition("START", new Transition("COOKING"));
        STATE_COOKING = new State(this,"COOKING");
        STATE_COOKING.addTransition("TIMER", new Transition("INACTIVE"));
        STATE_COOKING.addTransition("STOP", new Transition("INACTIVE"));
        STATE_COOKING.addTransition("OPEN", new Transition("DOOR_OPEN"));
        STATE_DOOR_OPEN = new State(this,"DOOR_OPEN");
        STATE_DOOR_OPEN.addTransition("CLOSE", new Transition("COOKING"));
        STATE_DOOR_OPEN.addTransition("STOP", new Transition("INACTIVE"));
    }
    
    @Override
    protected List<State> getAllStates() {
        return Arrays.asList(STATE_INACTIVE, STATE_COOKING, STATE_DOOR_OPEN);
    }

}
