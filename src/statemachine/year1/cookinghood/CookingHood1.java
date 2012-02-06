package statemachine.year1.cookinghood;

import javax.swing.JLabel;

import quickqui.QuickGUI;
import statemachine.year1.library.GraphicalMachine;

public class CookingHood1 extends GraphicalMachine {

    /**
     * GUI for cooking hood test
     */
    public static class ControlGUI extends QuickGUI.GUIModel {
        
        public static String POWER_ON_COMMAND = "__ON__";
        
        @Override 
        public void build() {
            frame("Cooking hood",Layout.VERTICAL,
                panel(Layout.HORIZONTAL,
                  label(text("Current state: ")),
                  label(name("state"),text("?"))),
                panel(Layout.HORIZONTAL,
                  label(text("Current motor power: ")),
                  label(name("power"),text("?"))),
                panel(Layout.HORIZONTAL,
                  label(text("Controls: ")),
                  button(name("PLUS"),text("(+)")),
                  button(name("MINUS"),text("(-)"))
                ),
                button(name(POWER_ON_COMMAND),text("Power on machine"))
              )
            ;
        }
    }

    public static void main(String argv[]) {
        new CookingHood1();
    }
    
    public CookingHood1() {
        super(new ControlGUI(),new CookingHoodMachine(),ControlGUI.POWER_ON_COMMAND);
    }

    /**
     * Handle updates to the state machine, display the current state in the GUI
     */
    @Override
    public void update() {
        ((JLabel)gui.getComponent("state")).setText(machine.getStateName());
        ((JLabel)gui.getComponent("power")).setText(new Integer(((CookingHoodMachine)machine).getPower()).toString());
    }

}
