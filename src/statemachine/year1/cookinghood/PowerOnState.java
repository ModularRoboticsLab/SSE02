package statemachine.year1.cookinghood;

import statemachine.year1.library.Event;

public class PowerOnState extends CookingHoodState {

    public PowerOnState(CookingHoodMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("PLUS")) {
            if(machine().getPower()==CookingHoodMachine.MAX_POWER)
                machine().setState(machine().MAX_POWER_STATE);
            else
                machine().setPower(machine().getPower()+1);
        } 
        else if(event.code().equals("MINUS")) {
            if(machine().getPower()==CookingHoodMachine.MIN_POWER)
                machine().setState(machine().POWER_OFF_STATE);
            else
                machine().setPower(machine().getPower()-1);
        }
        else
            throw new Error("Unknown event: "+event);
    }

}
