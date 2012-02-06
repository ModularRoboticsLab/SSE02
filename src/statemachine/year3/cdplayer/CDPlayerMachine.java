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

package statemachine.year3.cdplayer;

import statemachine.year3.dsl.FluentMachine;
import statemachine.year3.dsl.IntegerState;

public class CDPlayerMachine extends FluentMachine {

    // Extended state
    private IntegerState track;
    public int getTrack() { return track.value(); }
    
    // State machine definition
    @Override
    protected void build() {
        track = new IntegerState();
        state("STOP").
          transition("PLAY").to("PLAYING").setState(track,1).whenStateEquals(track, 0).
                             to("PLAYING").otherwise().
          transition("FORWARD").changeState(track,1).
          transition("BACK").changeState(track, -1).whenStateGreaterThan(track, 1).
        state("PLAYING").
          transition("STOP").to("STOP").setState(track,0).
          transition("PAUSE").to("PAUSED").
          transition("TRACK_END").changeState(track, 1);
        state("PAUSED").
          transition("STOP").to("STOP").
          transition("PLAY").to("PLAYING").
          transition("FORWARD").changeState(track,1).
          transition("BACK").changeState(track, -1).whenStateGreaterThan(track, 1)
        ;
    }

}
