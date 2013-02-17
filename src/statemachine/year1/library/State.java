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

/**
 * Abstract state class: can process events (subclasses must provide behavior),
 * keeps a reference to the machine to which it belongs to enable changing its state.
 * @author ups
 */
public abstract class State {

	/**
	 * The state machine to which this state belongs
	 */
    private IMachine machine;

    /**
     * Create a state belonging to the given state machine
     * @param machine the state machine to which this state belongs
     */
    public State(IMachine machine) {
        this.machine = machine;
    }

    /**
     * Process the given event according to the state
     * @param event an incoming event
     */
    public abstract void processEvent(Event event);
    
    /**
     * Get the statemachine to which this state belongs
     * @return
     */
    public IMachine machine() {
        return machine;
    }

    /**
     * Get a representation of the name of the currently active state
     */
    public String toString() {
        String name = this.getClass().getSimpleName();
        if(name==null) name = this.getClass().getName();
        return name;
    }
}
