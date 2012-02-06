package statemachine.year3.dsl;

/**
 * A simple mutable integer state
 * @author ups
 */
public class IntegerState {

    private int value;
    
    public int value() {
        return value;
    }

    public void set(int x) {
        value = x;
    }

}
