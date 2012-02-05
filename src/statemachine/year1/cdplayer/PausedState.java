package statemachine.year1.cdplayer;

import statemachine.year1.library.Event;

public class PausedState extends CDPlayerState {

    public PausedState(CDPlayerMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("PLAY")) {
            machine().setState(machine().PLAYING_STATE);
        } 
        else if(event.code().equals("STOP")) {
            machine().setTrack(0);
            machine().setState(machine().STOPPED_STATE);
        }
        else
            super.processIfForwardOrBack(event);
    }

}
