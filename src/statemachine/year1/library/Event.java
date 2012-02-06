package statemachine.year1.library;

/**
 * Generic event class, representing incoming event as a string
 * @author ups
 */
public class Event {

    private String code;
    
    public Event(String code) {
        this.code = code;
    }
    
    public String code() {
        return code;
    }

}
