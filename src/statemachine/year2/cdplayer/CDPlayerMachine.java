package statemachine.year2.cdplayer;

import java.util.Arrays;
import java.util.List;

import statemachine.year2.framework.Machine;
import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;

public class CDPlayerMachine extends Machine {

    private State STATE_STOP, STATE_PLAYING, STATE_PAUSED;
    
    private int track;
    
    public int getTrack() { return track; }
    
    public CDPlayerMachine() {
        Transition forward = new Transition(null) {
            @Override public void effect() { track++; }
        };
        Transition backward = new Transition(null) {
            @Override public boolean isApplicable() { return track>1; }
            @Override public void effect() { track--; }
        };
        Transition stop = new Transition("STOP") {
            @Override public void effect() { track=0; } 
        };
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
