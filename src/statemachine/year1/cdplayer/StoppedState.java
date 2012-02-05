package statemachine.year1.cdplayer;

import statemachine.year1.library.Event;

public class StoppedState extends CDPlayerState {

    public StoppedState(CDPlayerMachine machine) {
        super(machine);
    }

    @Override
    public void processEvent(Event event) {
        if(event.code().equals("PLAY")) {
            if(machine().getTrack()==0)
                machine().setTrack(1);
            machine().setState(machine().PLAYING_STATE);
        }
        else
            super.processIfForwardOrBack(event);
    }

}
