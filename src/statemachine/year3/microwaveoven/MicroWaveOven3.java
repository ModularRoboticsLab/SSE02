package statemachine.year3.microwaveoven;

import javax.swing.JLabel;

import statemachine.year1.library.GraphicalMachine;
import statemachine.year1.microwaveoven.MicroWaveOven1.ControlGUI;

public class MicroWaveOven3 extends GraphicalMachine {

    public static void main(String argv[]) {
        new MicroWaveOven3();
    }
    
    public MicroWaveOven3() {
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
