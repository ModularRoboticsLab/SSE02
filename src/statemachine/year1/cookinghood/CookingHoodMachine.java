package statemachine.year1.cookinghood;

import statemachine.year1.library.Machine;
import statemachine.year1.library.State;

public class CookingHoodMachine extends Machine {
    public static final int MAX_POWER = 6;
    public static final int MIN_POWER = 1;
    private int power = 0;
    
    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }
    
    public final State POWER_OFF_STATE = new PowerOffState(this);
    public final State POWER_ON_STATE = new PowerOnState(this);
    public final State MAX_POWER_STATE = new MaxPowerState(this);
    
    @Override
    protected State getInitialState() {
        return POWER_OFF_STATE;
    }
}
