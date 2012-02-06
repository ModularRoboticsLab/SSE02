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

package statemachine.year2.microwaveoven;

import java.util.Arrays;
import java.util.List;

import statemachine.year2.framework.Machine;
import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;

public class MicrowaveMachine extends Machine {

    // States
    private State STATE_INACTIVE, STATE_COOKING, STATE_DOOR_OPEN;
    
    // State machine definition
    public MicrowaveMachine() {
        STATE_INACTIVE = new State(this,"INACTIVE");
        STATE_INACTIVE.addTransition("START", new Transition("COOKING"));
        STATE_COOKING = new State(this,"COOKING");
        STATE_COOKING.addTransition("TIMER", new Transition("INACTIVE"));
        STATE_COOKING.addTransition("STOP", new Transition("INACTIVE"));
        STATE_COOKING.addTransition("OPEN", new Transition("DOOR_OPEN"));
        STATE_DOOR_OPEN = new State(this,"DOOR_OPEN");
        STATE_DOOR_OPEN.addTransition("CLOSE", new Transition("COOKING"));
        STATE_DOOR_OPEN.addTransition("STOP", new Transition("INACTIVE"));
    }
    
    @Override
    protected List<State> getAllStates() {
        return Arrays.asList(STATE_INACTIVE, STATE_COOKING, STATE_DOOR_OPEN);
    }

}
