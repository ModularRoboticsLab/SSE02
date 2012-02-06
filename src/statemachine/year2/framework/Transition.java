package statemachine.year2.framework;

/**
 * Generic template class for transitions.  Simple transitions can use this class directly,
 * conditions can be added by overriding the isApplicable method, effects can be added by
 * overriding the effect method.
 * @author ups
 */
public class Transition {

    private String targetState;

    public Transition(String targetState) {
        this.targetState = targetState;
    }
    
    public String action() {
        effect();
        return targetState;
    }

    /**
     * Provide name of target state that should be transitioned to
     * @return
     */
    public String getTarget() {
        return targetState;
    }

    /**
     * Hook method: override to provide effect 
     */
    protected void effect() { ; }

    /**
     * Hook method: override to provide condition
     * @return false if this transition should not take place now, true otherwise
     */
    protected boolean isApplicable() {
        return true;
    }

}
