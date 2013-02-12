/*
Copyright (c) 2012, Ulrik Pagh Schultz, University of Southern Denmark
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the University of Southern Denmark.
*/

package examples.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JLabel;

import examples.communication.Driver;
import quickqui.QuickGUI;

/**
 * Small, canonical GUI example: the calculator
 * @author ups
 */
public class Calculator implements ActionListener {
    
    /**
     * The specific GUI to use for this application
     * @author ups
     */
    public static class CalculatorGUI extends QuickGUI.GUIModel {
        
        @Override 
        public void build() {
            frame("Calculator",Layout.VERTICAL,
            	panel(Layout.HORIZONTAL,
            		label(name("display"),text("0"))),
            	panel(Layout.HORIZONTAL,
            		button(name("1"),text("1")),
            		button(name("2"),text("2")),
            		button(name("3"),text("3"))),
                panel(Layout.HORIZONTAL,
                	button(name("4"),text("4")),
                    button(name("5"),text("5")),
                    button(name("6"),text("6"))),
                panel(Layout.HORIZONTAL,
                    button(name("7"),text("7")),
                    button(name("8"),text("8")),
                    button(name("9"),text("9"))),
                panel(Layout.HORIZONTAL,
                	button(name("clear"),text("CLR")),
                	button(name("plus"),text("+")),
                	button(name("equals"),text("="))))
            ;
        }
    }

    /**
     * Create GUI and then wait for events
     */
    public static void main(String argv[]) throws IOException {
        Calculator calc = new Calculator();
        calc.gui = new QuickGUI(new CalculatorGUI(),calc);
    }

    /**
     * The GUI 
     */
    private QuickGUI gui;

    /**
     * Regexp pattern matcher for detecting number button events
     */
    private static final Pattern numbers = Pattern.compile("[0-9]");
    
    /**
     * Respond to GUI events
     */
    @Override
    public void actionPerformed(ActionEvent event) {
    	String button = event.getActionCommand();
    	if("plus".equals(button)) plusAction();
    	else if("equals".equals(button)) equalsAction();
    	else if("clear".equals(button)) clearAction();
    	else if(numbers.matcher(button).matches()) numberAction(Integer.parseInt(button));
        else System.err.println("Warning: unknown event "+event);
    	updateDisplay();
    }

	/**
     * Update calculator display
     */
    private void setDisplay(String content) {
    	JLabel display = (JLabel)gui.getComponent("display");
    	display.setText(content);
    }
    
    /**
     * Calculator logic
     */

    /**
     * Accumulator is the number being added to, current is the number in the display
     */
    private int accumulator, current;
    
    /**
     * Change the value of current according to the number entered in the display
     * @param n the number entered
     */
	private void numberAction(int n) {
		current = current*10+n;
	}

	/**
	 * Update current from the accumulator and reset the accumulator 
	 */
	private void equalsAction() {
		current += accumulator;
		accumulator = 0;
	}

	/**
	 * Increment accumulator according to current
	 */
	private void plusAction() {
		accumulator += current;
		current = 0;
	}
	
	/**
	 * Clear the display and the accumulator
	 */
    private void clearAction() {
    	accumulator = current = 0;
	}

    /**
     * Update the value in the display to show the value of current
     */
    private void updateDisplay() {
    	setDisplay(Integer.toString(current));
    }
}
