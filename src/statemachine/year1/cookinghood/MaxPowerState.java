package statemachine.year1.cookinghood;

import statemachine.year1.library.Event;

public class MaxPowerState extends CookingHoodState {

    public MaxPowerState(CookingHoodMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("PLUS")) {
            // ignore
        } 
        else if(event.code().equals("MINUS")) {
            machine().setPower(CookingHoodMachine.MAX_POWER);
            machine().setState(machine().POWER_ON_STATE);
        }
        else
            throw new Error("Unknown event: "+event);
    }

}
