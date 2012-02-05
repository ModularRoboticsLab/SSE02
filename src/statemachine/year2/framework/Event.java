package statemachine.year2.framework;

public class Event {

    private String code;
    
    public Event(String code) {
        this.code = code;
    }
    
    public String code() {
        return code;
    }

}
