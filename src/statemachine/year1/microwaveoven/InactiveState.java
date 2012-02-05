package statemachine.year1.microwaveoven;

import statemachine.year1.library.Event;

public class InactiveState extends MicrowaveOvenState {

    public InactiveState(MicrowaveMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("START")) {
            machine().setState(machine().COOKING_STATE);
        } 
        else
            ; // ignore 
    }

}
