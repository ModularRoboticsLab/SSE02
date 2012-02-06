/*
Copyright (c) 2012, Ulrik Pagh Schultz, University of Southern Denmark
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the University of Southern Denmark.
*/

package statemachine.year2.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import statemachine.year1.library.Event;
import statemachine.year1.library.IMachine;

/**
 * State machine: keeps track of current state, stores a map from state name
 * to state object (used to perform state transitions).
 * @author ups
 */
public abstract class Machine extends Observable implements IMachine {

    /**
     * Current state
     */
    private State currentState;
    /**
     * Map from state name to state object
     */
    private Map<String,State> states = new HashMap<String,State>();
    
    public void initialize() {
        List<State> allStates = getAllStates();
        for(State state: allStates)
            states.put(state.getName(), state);
        setState(allStates.get(0).getName());
        setChanged();
        notifyObservers();
    }
    
    public void setState(String stateid) {
        State state = states.get(stateid);
        if(state==null) throw new Error("Illegal state identifier: "+stateid);
        currentState = state;
    }

    public String getStateName() {
        return currentState.toString();
    }

    
    public void processEvent(Event event) {
        if(currentState==null) throw new Error("State machine not initialized");
        currentState.processEvent(event);
        setChanged();
        notifyObservers();
    }
    
    /**
     * Overridden by concrete state machine.  By convention the first element must be
     * the initial state.
     * @return List of all states, first element is initial state
     */
    protected abstract List<State> getAllStates();

}
