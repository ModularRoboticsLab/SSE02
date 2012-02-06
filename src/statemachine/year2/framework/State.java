package statemachine.year2.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import statemachine.year1.library.Event;

/**
 * A generic state representation: belongs to a machine, has a name, and a number
 * of transitions.  When an event is delivered, the transitions are tested in the
 * same order as they were initially inserted into the state, the first one that is
 * applicable is performed by executing its effect (if any) and performing the
 * corresponding transition (if any).
 * @author ups
 */
public class State {

    /**
     * The machine to which this state belongs
     */
    private Machine machine;
    /**
     * The name of the state
     */
    private String name;
    /**
     * Transitions associated with this state
     */
    private Map<String,List<Transition>> transitions = new HashMap<String,List<Transition>>();

    public State(Machine machine, String name) {
        this.machine = machine;
        this.name = name;
    }

    /**
     * Add a transition that is tested in this state when the corresponding event is received
     * @param eventName name of event that can trigger this transition
     * @param transition the transition
     */
    public void addTransition(String eventName, Transition transition) {
        List<Transition> matches = transitions.get(eventName);
        if(matches==null) {
            matches = new ArrayList<Transition>();
            transitions.put(eventName, matches);
        }
        matches.add(transition);
    }
    
    /**
     * Process event by testing isApplicable in sequence, and then performing the action which
     * may have an effect and returns the name of a new state, if a transition is to be made.
     */
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
