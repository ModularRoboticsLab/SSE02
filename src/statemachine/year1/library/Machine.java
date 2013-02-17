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

package statemachine.year1.library;

import java.util.Observable;

/**
 * Abstract state machine class: has a current state, starts in initial state,
 * and can process events by sending them to the current state
 * @author ups
 */
public abstract class Machine extends Observable implements IMachine {
    
	/**
	 * The currently active state
	 */
    private State currentState;
    
    /**
     * Initialize the state machine
     */
    public void initialize() {
        setState(getInitialState());
        setChanged();
        notifyObservers();
    }
    
    /**
     * Directly set the current state of the state machine
     * @param state the future current state
     */
    public void setState(State state) {
        currentState = state;
    }

    /**
     * Get the name of the currently active state
     */
    public String getStateName() {
        return currentState.toString();
    }

    /**
     * Process an incoming event, triggering appropriate transitions depending on the current state
     */
    public void processEvent(Event event) {
        if(currentState==null) throw new Error("State machine not initialized");
        currentState.processEvent(event);
        setChanged();
        notifyObservers();
    }
    
    /**
     * Get the initial state of the state machine
     * @return the initial state of the state machine
     */
    protected abstract State getInitialState();

}
