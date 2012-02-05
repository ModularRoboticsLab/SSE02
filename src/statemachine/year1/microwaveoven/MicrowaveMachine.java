package statemachine.year1.microwaveoven;

import statemachine.year1.library.Machine;
import statemachine.year1.library.State;

public class MicrowaveMachine extends Machine {
    
    public final State INACTIVE_STATE = new InactiveState(this);
    public final State COOKING_STATE = new CookingState(this);
    public final State DOOR_OPEN_STATE = new DoorOpenState(this);
    
    @Override
    protected State getInitialState() {
        return INACTIVE_STATE;
    }
}
