package statemachine.year1.cookinghood;

import statemachine.year1.library.State;

public abstract class CookingHoodState extends State {

    public CookingHoodState(CookingHoodMachine machine) {
        super(machine);
    }

    @Override
    public CookingHoodMachine machine() {
        return (CookingHoodMachine)super.machine();
    }
    
}
