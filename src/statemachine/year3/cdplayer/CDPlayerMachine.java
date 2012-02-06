package statemachine.year3.cdplayer;

import statemachine.year3.dsl.FluentMachine;
import statemachine.year3.dsl.IntegerState;

public class CDPlayerMachine extends FluentMachine {

    private IntegerState track;
    
    public int getTrack() { return track.value(); }
    
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
