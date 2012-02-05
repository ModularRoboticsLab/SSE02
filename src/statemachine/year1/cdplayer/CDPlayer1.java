package statemachine.year1.cdplayer;

import javax.swing.JLabel;

import quickqui.QuickGUI;
import statemachine.year1.library.GraphicalMachine;

public class CDPlayer1 extends GraphicalMachine {

    public static class ControlGUI extends QuickGUI.GUIModel {
        
        public static String POWER_ON_COMMAND = "__ON__";
        
        @Override 
        public void build() {
            frame("CD Player",Layout.VERTICAL,
                panel(Layout.HORIZONTAL,
                  label(text("Current state: ")),
                  label(name("state"),text("?"))),
                panel(Layout.HORIZONTAL,
                  label(text("Current track: ")),
                  label(name("track"),text("?"))),
                panel(Layout.HORIZONTAL,
                  label(text("Controls: ")),
                  button(name("PLAY"),text("Play")),
                  button(name("STOP"),text("Stop")),
                  button(name("PAUSE"),text("Pause")),
                  button(name("FORWARD"),text("Forward")),
                  button(name("BACK"),text("Back"))),
                panel(Layout.HORIZONTAL,
                  label(text("Events: ")),
                  button(name("TRACK_END"),text("End of track"))),
                button(name(POWER_ON_COMMAND),text("Power on machine"))
              )
            ;
        }
    }

    /**
     * Create GUI and then activate robot server functionality
     */
    public static void main(String argv[]) {
        new CDPlayer1();
    }
    
    public CDPlayer1() {
        super(new ControlGUI(),new CDPlayerMachine(),ControlGUI.POWER_ON_COMMAND);
    }

    @Override
    public void update() {
        ((JLabel)gui.getComponent("state")).setText(machine.getState().toString());
        ((JLabel)gui.getComponent("track")).setText(new Integer(((CDPlayerMachine)machine).getTrack()).toString());
    }

}
