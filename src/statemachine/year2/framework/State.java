package statemachine.year2.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import statemachine.year1.library.Event;

public class State {

    private Machine machine;
    private String name;
    private Map<String,List<Transition>> transitions = new HashMap<String,List<Transition>>();

    public State(Machine machine, String name) {
        this.machine = machine;
        this.name = name;
    }

    public void addTransition(String eventName, Transition transition) {
        List<Transition> matches = transitions.get(eventName);
        if(matches==null) {
            matches = new ArrayList<Transition>();
            transitions.put(eventName, matches);
        }
        matches.add(transition);
    }
    
    public void processEvent(Event event) {
        List<Transition> matches = transitions.get(event.code());
        if(matches==null) return;
        for(Transition tran: matches)
            if(tran.isApplicable()) { 
                String newMaybe = tran.action();
                if(newMaybe!=null) machine().setState(newMaybe);
                return;
            }
    }
    
    public Machine machine() {
        return machine;
    }

    public String toString() {
        return this.getName();
    }

    public String getName() {
        return name;
    }
}
