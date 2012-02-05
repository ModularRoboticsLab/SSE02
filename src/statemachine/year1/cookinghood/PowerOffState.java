package statemachine.year1.cookinghood;

import statemachine.year1.library.Event;

public class PowerOffState extends CookingHoodState {

    public PowerOffState(CookingHoodMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("PLUS")) {
            machine().setPower(CookingHoodMachine.MIN_POWER);
            machine().setState(machine().POWER_ON_STATE);
        } 
        else if(event.code().equals("MINUS")) {
            // ignore
        }
        else
            throw new Error("Unknown event: "+event);
    }

}
