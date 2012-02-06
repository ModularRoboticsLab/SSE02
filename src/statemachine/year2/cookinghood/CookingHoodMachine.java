package statemachine.year2.cookinghood;

import java.util.Arrays;
import java.util.List;

import statemachine.year2.framework.Machine;
import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;

public class CookingHoodMachine extends Machine {

    private static final int MIN_POWER = 1;
    private static final int MAX_POWER = 6;

    private State STATE_POWER_OFF, STATE_POWER_ON, STATE_MAX_POWER;
    
    private int power;
    
    public int getPower() { return power; }
    
    public CookingHoodMachine() {
        STATE_POWER_OFF = new State(this,"POWER_OFF");
        STATE_POWER_OFF.addTransition("PLUS", new Transition("POWER_ON") { 
            @Override public void effect() { power=MIN_POWER; }
        });
        STATE_POWER_ON = new State(this,"POWER_ON");
        STATE_POWER_ON.addTransition("PLUS", new Transition("MAX_POWER") {
            @Override public boolean isApplicable() { return power==MAX_POWER; } 
        });
        STATE_POWER_ON.addTransition("PLUS", new Transition(null) {
            @Override public void effect() { power++; }
        });
        STATE_POWER_ON.addTransition("MINUS", new Transition("POWER_OFF") {
           @Override public boolean isApplicable() { return power==MIN_POWER; } 
        });
        STATE_POWER_ON.addTransition("MINUS", new Transition(null) {
            @Override public void effect() { power--; }
        });
        STATE_MAX_POWER = new State(this,"MAX_POWER");
        STATE_MAX_POWER.addTransition("MINUS", new Transition("POWER_ON") {
            @Override public void effect() { power=MAX_POWER; }
        });
    }
    
    @Override
    protected List<State> getAllStates() {
        return Arrays.asList(STATE_POWER_OFF, STATE_POWER_ON, STATE_MAX_POWER);
    }

}
