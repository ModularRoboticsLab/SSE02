package statemachine.year3.cookinghood;

import statemachine.year3.dsl.FluentMachine;
import statemachine.year3.dsl.IntegerState;

public class CookingHoodMachine extends FluentMachine {

    // Constants
    private static final int MIN_POWER = 1;
    private static final int MAX_POWER = 6;

    // Extended state
    private IntegerState power;
    public int getPower() { return power.value(); }
    
    // State machine definition
    @Override
    protected void build() {
        power = new IntegerState();
        state("POWER_OFF").
          transition("PLUS").to("POWER_ON").setState(power,MIN_POWER).
        state("POWER_ON").
          transition("PLUS").to("MAX_POWER").whenStateEquals(power,MAX_POWER).
                             changeState(power,1).otherwise().
          transition("MINUS").to("POWER_OFF").whenStateEquals(power,MIN_POWER).
                             changeState(power,-1).otherwise().
        state("MAX_POWER").
          transition("MINUS").to("POWER_ON").setState(power,MAX_POWER)
        ;
    }

}
