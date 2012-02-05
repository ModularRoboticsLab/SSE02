package statemachine.year1.cdplayer;

import statemachine.year1.library.Event;

public class PlayingState extends CDPlayerState {

    public PlayingState(CDPlayerMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("PAUSE")) {
            machine().setState(machine().PAUSED_STATE);
        } 
        else if(event.code().equals("STOP")) {
            machine().setTrack(0);
            machine().setState(machine().STOPPED_STATE);
        }
        else if(event.code().equals("TRACK_END")) {
            machine().setTrack(machine().getTrack()+1);
        }
        else
            ; // ignore
    }

}
