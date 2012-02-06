package statemachine.year1.cookinghood;

import statemachine.year1.library.State;

/**
 * Abstract helper class: provides typed access to the state machine.
 * @author ups
 */
public abstract class CookingHoodState extends State {

    public CookingHoodState(CookingHoodMachine machine) {
        super(machine);
    }

    @Override
    public CookingHoodMachine machine() {
        return (CookingHoodMachine)super.machine();
    }
    
}
