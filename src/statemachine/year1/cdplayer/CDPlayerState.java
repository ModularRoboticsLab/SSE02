package statemachine.year1.cdplayer;

import statemachine.year1.library.Event;
import statemachine.year1.library.State;

public abstract class CDPlayerState extends State {

    public CDPlayerState(CDPlayerMachine machine) {
        super(machine);
    }

    @Override
    public CDPlayerMachine machine() {
        return (CDPlayerMachine)super.machine();
    }
    
    protected void processIfForwardOrBack(Event event) {
        if(event.code().equals("FORWARD")) {
            machine().setTrack(machine().getTrack()+1);
        }
        else if(event.code().equals("BACK")) {
            if(machine().getTrack()>0)
                machine().setTrack(machine().getTrack()-1);
        }
    }
}
