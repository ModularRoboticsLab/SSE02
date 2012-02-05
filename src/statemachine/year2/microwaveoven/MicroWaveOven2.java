package statemachine.year2.microwaveoven;

import javax.swing.JLabel;

import statemachine.year2.framework.GraphicalMachine;
import statemachine.year1.microwaveoven.MicroWaveOven1.ControlGUI;

public class MicroWaveOven2 extends GraphicalMachine {

    /**
     * Create GUI and then activate robot server functionality
     */
    public static void main(String argv[]) {
        new MicroWaveOven2();
    }
    
    public MicroWaveOven2() {
        super(new ControlGUI(),new MicrowaveMachine(),ControlGUI.POWER_ON_COMMAND);
    }

    @Override
    public void update() {
        ((JLabel)gui.getComponent("state")).setText(machine.getState().toString());
    }

}
