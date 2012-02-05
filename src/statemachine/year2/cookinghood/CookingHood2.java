package statemachine.year2.cookinghood;

import javax.swing.JLabel;

import quickqui.QuickGUI;
import statemachine.year2.framework.GraphicalMachine;
import statemachine.year1.cookinghood.CookingHood1.ControlGUI;

public class CookingHood2 extends GraphicalMachine {

    /**
     * Create GUI and then activate robot server functionality
     */
    public static void main(String argv[]) {
        new CookingHood2();
    }
    
    public CookingHood2() {
        super(new ControlGUI(),new CookingHoodMachine(),ControlGUI.POWER_ON_COMMAND);
    }

    @Override
    public void update() {
        ((JLabel)gui.getComponent("state")).setText(machine.getState().toString());
        ((JLabel)gui.getComponent("power")).setText(new Integer(((CookingHoodMachine)machine).getPower()).toString());
    }

}
