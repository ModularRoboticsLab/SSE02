package statemachine.year1.microwaveoven;

import statemachine.year1.library.State;

public abstract class MicrowaveOvenState extends State {

    public MicrowaveOvenState(MicrowaveMachine machine) {
        super(machine);
    }

    @Override
    public MicrowaveMachine machine() {
        return (MicrowaveMachine)super.machine();
    }
    
}
