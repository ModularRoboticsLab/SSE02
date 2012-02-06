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

package statemachine.year2.cdplayer;

import java.util.Arrays;
import java.util.List;

import statemachine.year2.framework.Machine;
import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;

public class CDPlayerMachine extends Machine {

    // States
    private State STATE_STOP, STATE_PLAYING, STATE_PAUSED;
    
    // Extended state
    private int track;
    public int getTrack() { return track; }

    // State machine definition
    public CDPlayerMachine() {
        // Forward and backward transitions are identical in STOPPED and PAUSED, share
        Transition forward = new Transition(null) {
            @Override public void effect() { track++; }
        };
        Transition backward = new Transition(null) {
            @Override public boolean isApplicable() { return track>1; }
            @Override public void effect() { track--; }
        };
        // Stop transition is identical in PLAYING and PAUSED, share
        Transition stop = new Transition("STOP") {
            @Override public void effect() { track=0; } 
        };
        // Define states and transitions
        STATE_STOP = new State(this,"STOP");
        STATE_STOP.addTransition("PLAY", new Transition("PLAYING") {
           @Override public void effect() { if(track==0) track=1; } 
        });
        STATE_STOP.addTransition("FORWARD", forward);
        STATE_STOP.addTransition("BACK", backward);
        STATE_PLAYING = new State(this,"PLAYING");
        STATE_PLAYING.addTransition("STOP", stop);
        STATE_PLAYING.addTransition("PAUSE", new Transition("PAUSED"));
        STATE_PLAYING.addTransition("TRACK_END", new Transition(null) {
            @Override public void effect() { track++; }
        });
        STATE_PAUSED = new State(this,"PAUSED");
        STATE_PAUSED.addTransition("PLAY", new Transition("PLAYING"));
        STATE_PAUSED.addTransition("STOP", stop);
        STATE_PAUSED.addTransition("FORWARD", forward);
        STATE_PAUSED.addTransition("BACK", backward);
    }
    
    @Override
    protected List<State> getAllStates() {
        return Arrays.asList(STATE_STOP, STATE_PLAYING, STATE_PAUSED);
    }

}
