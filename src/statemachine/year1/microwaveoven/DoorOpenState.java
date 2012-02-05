package statemachine.year1.microwaveoven;

import statemachine.year1.library.Event;

public class DoorOpenState extends MicrowaveOvenState {

    public DoorOpenState(MicrowaveMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("STOP")) {
            machine().setState(machine().INACTIVE_STATE);
        } 
        else if(event.code().equals("CLOSE")) {
            machine().setState(machine().COOKING_STATE);
        }
        else
            ; // ignore
    }

}
