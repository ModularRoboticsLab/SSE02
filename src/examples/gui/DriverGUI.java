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
import javax.swing.JLabel;

import examples.communication.Driver;
import quickqui.QuickGUI;

/**
 * Extension of the robot driver server to display information in GUI and make
 * control of bumper sensor available as a user interface
 * @author ups
 */
public class DriverGUI extends Driver implements ActionListener {
    
    /**
     * The specific GUI to use for this application
     * @author ups
     */
    public static class ControlGUI extends QuickGUI.GUIModel {
        
        @Override 
        public void build() {
            frame("Robot",Layout.VERTICAL,
                label(name("left"),text("Speed of left motor")),
                label(name("right"),text("Speed of right motor")),
                panel(Layout.HORIZONTAL,
                  label(text("Bump sensor: ")),
                  button(name("bumpLeft"),text("Left")),
                  button(name("bumpCenter"),text("Center")),
                  button(name("bumpRight"),text("Right"))
                ),
                label(name("status"),text("Initializing"))
              )
            ;
        }
    }

    /**
     * Create GUI and then activate robot server functionality
     */
    public static void main(String argv[]) throws IOException {
        DriverGUI driver = new DriverGUI();
        driver.gui = new QuickGUI(new ControlGUI(),driver);
        driver.main();
    }

    /**
     * Bumper sensor memory: contains last value of bumper sensor triggered, "NONE" otherwise
     */
    private String bumpSensor = "NONE";
    
    /**
     * The GUI 
     */
    private QuickGUI gui;
    
    /**
     * Respond to GUI events
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getActionCommand().equals("bumpLeft")) bumpSensor = "LEFT";
        else if(event.getActionCommand().equals("bumpCenter")) bumpSensor = "CENTER";
        else if(event.getActionCommand().equals("bumpRight")) bumpSensor = "RIGHT";
        else System.err.println("Warning: unknown event "+event);
    }

    /**
     * Respond to a robot drive command received over the network
     */
    protected void robotDrive(int left, int right) {
        JLabel leftLabel = (JLabel)gui.getComponent("left");
        JLabel rightLabel = (JLabel)gui.getComponent("right");
        leftLabel.setText("Left="+left);
        rightLabel.setText("Right="+right);
        System.out.println("Driving at speed "+left+","+right);
    }

    /**
     * Respond to a robot stop command received over the network
     */
    protected void robotStop() {
        JLabel leftLabel = (JLabel)gui.getComponent("left");
        JLabel rightLabel = (JLabel)gui.getComponent("right");
        leftLabel.setText("Left: stopped");
        rightLabel.setText("Right: stopped");
        System.out.println("Robot stopped");
    }

    /**
     * Respond to a read bump sensor event received over the network
     */
    protected boolean readBumpSensor(String sensor) {
        return sensor.equals(bumpSensor);
    }

}
