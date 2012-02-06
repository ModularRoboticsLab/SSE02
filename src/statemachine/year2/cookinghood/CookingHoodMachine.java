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

package statemachine.year2.cookinghood;

import java.util.Arrays;
import java.util.List;

import statemachine.year2.framework.Machine;
import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;

public class CookingHoodMachine extends Machine {

    // Constants
    private static final int MIN_POWER = 1;
    private static final int MAX_POWER = 6;

    // States
    private State STATE_POWER_OFF, STATE_POWER_ON, STATE_MAX_POWER;
    
    // Extended state
    private int power;
    public int getPower() { return power; }
    
    // State machine definition
    public CookingHoodMachine() {
        STATE_POWER_OFF = new State(this,"POWER_OFF");
        STATE_POWER_OFF.addTransition("PLUS", new Transition("POWER_ON") { 
            @Override public void effect() { power=MIN_POWER; }
        });
        STATE_POWER_ON = new State(this,"POWER_ON");
        STATE_POWER_ON.addTransition("PLUS", new Transition("MAX_POWER") {
            @Override public boolean isApplicable() { return power==MAX_POWER; } 
        });
        STATE_POWER_ON.addTransition("PLUS", new Transition(null) {
            @Override public void effect() { power++; }
        });
        STATE_POWER_ON.addTransition("MINUS", new Transition("POWER_OFF") {
           @Override public boolean isApplicable() { return power==MIN_POWER; } 
        });
        STATE_POWER_ON.addTransition("MINUS", new Transition(null) {
            @Override public void effect() { power--; }
        });
        STATE_MAX_POWER = new State(this,"MAX_POWER");
        STATE_MAX_POWER.addTransition("MINUS", new Transition("POWER_ON") {
            @Override public void effect() { power=MAX_POWER; }
        });
    }
    
    @Override
    protected List<State> getAllStates() {
        return Arrays.asList(STATE_POWER_OFF, STATE_POWER_ON, STATE_MAX_POWER);
    }

}
