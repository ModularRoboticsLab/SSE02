package statemachine.year1.microwaveoven;

import javax.swing.JLabel;

import quickqui.QuickGUI;
import statemachine.year1.library.GraphicalMachine;

public class MicroWaveOven1 extends GraphicalMachine {

    /**
     * GUI for microwave oven test
     */
    public static class ControlGUI extends QuickGUI.GUIModel {
        
        public static String POWER_ON_COMMAND = "__ON__";
        
        @Override 
        public void build() {
            frame("Microwave oven",Layout.VERTICAL,
                panel(Layout.HORIZONTAL,
                  label(text("Current state: ")),
                  label(name("state"),text("?"))),
                panel(Layout.HORIZONTAL,
                  label(text("Controls: ")),
                  button(name("START"),text("Start")),
                  button(name("STOP"),text("Stop"))
                ),
                panel(Layout.HORIZONTAL,
                  label(text("Door: ")),
                  button(name("OPEN"),text("Open")),
                  button(name("CLOSE"),text("Close"))
                ),
                panel(Layout.HORIZONTAL,
                  label(text("Timer: ")),
                  button(name("TIMER"),text("Trigger"))
                ),
                button(name(POWER_ON_COMMAND),text("Power on machine"))
              )
            ;
        }
    }

    public static void main(String argv[]) {
        new MicroWaveOven1();
    }
    
    public MicroWaveOven1() {
        super(new ControlGUI(),new MicrowaveMachine(),ControlGUI.POWER_ON_COMMAND);
    }

    /**
     * Handle updates to the state machine, display the current state in the GUI
     */
    @Override
    public void update() {
        ((JLabel)gui.getComponent("state")).setText(machine.getStateName());
    }

}
