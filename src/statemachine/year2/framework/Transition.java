package statemachine.year2.framework;

public class Transition {

    private String targetState;

    public Transition(String targetState) {
        this.targetState = targetState;
    }
    
    public boolean isApplicable() {
        return true;
    }

    public String action() {
        effect();
        return targetState;
    }

    protected void effect() { ; }

}
