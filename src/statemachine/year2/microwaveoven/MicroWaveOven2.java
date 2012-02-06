package statemachine.year2.microwaveoven;

import javax.swing.JLabel;

import statemachine.year1.library.GraphicalMachine;
import statemachine.year1.microwaveoven.MicroWaveOven1.ControlGUI;

public class MicroWaveOven2 extends GraphicalMachine {

    public static void main(String argv[]) {
        new MicroWaveOven2();
    }
    
    public MicroWaveOven2() {
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
