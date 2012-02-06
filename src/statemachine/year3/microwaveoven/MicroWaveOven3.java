package statemachine.year3.microwaveoven;

import javax.swing.JLabel;

import statemachine.year2.framework.GraphicalMachine;
import statemachine.year1.microwaveoven.MicroWaveOven1.ControlGUI;

public class MicroWaveOven3 extends GraphicalMachine {

    /**
     * Create GUI and then activate robot server functionality
     */
    public static void main(String argv[]) {
        new MicroWaveOven3();
    }
    
    public MicroWaveOven3() {
        super(new ControlGUI(),new MicrowaveMachine(),ControlGUI.POWER_ON_COMMAND);
    }

    @Override
    public void update() {
        ((JLabel)gui.getComponent("state")).setText(machine.getState().toString());
    }

}
