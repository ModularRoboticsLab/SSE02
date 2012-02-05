package statemachine.year1.microwaveoven;

import statemachine.year1.library.Event;

public class CookingState extends MicrowaveOvenState {

    public CookingState(MicrowaveMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("TIMER") || event.code().equals("STOP")) {
            machine().setState(machine().INACTIVE_STATE);
        } 
        else if(event.code().equals("OPEN")) {
            machine().setState(machine().DOOR_OPEN_STATE);
        }
        else
            ; // ignore
    }

}
