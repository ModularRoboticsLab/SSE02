package statemachine.year1.cdplayer;

import statemachine.year1.library.Machine;
import statemachine.year1.library.State;

public class CDPlayerMachine extends Machine {
    public static final int MAX_POWER = 6;
    public static final int MIN_POWER = 1;
    private int power = 0;
    
    public int getTrack() { return power; }
    public void setTrack(int power) { this.power = power; }
    
    public final State PLAYING_STATE = new PlayingState(this);
    public final State PAUSED_STATE = new PausedState(this);
    public final State STOPPED_STATE = new StoppedState(this);
    
    @Override
    protected State getInitialState() {
        return STOPPED_STATE;
    }
}
