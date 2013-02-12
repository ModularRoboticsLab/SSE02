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

package examples.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import serializer.Communicator;
import serializer.IPacketHandler;
import serializer.IncomingPacket;
import serializer.PacketFormat;
import serializer.Packetizer;

/**
 * A simple textual example of a robot driver (server, the simulated robot) that prints the actions of 
 * the robot to the screen, and responds correctly to a stop invocation by sending an acknowledge.  The 
 * specific operations are separated into methods template-class style.
 * @author ups
 */
public class Driver implements IPacketHandler {

    /**
     * Create the server socket for communication with a controller, localhost on port 444
     * @return newly created socket
     */
    private static Socket getCommunicationChannel() { 
        try {
            ServerSocket serverSocket = new ServerSocket(4444);
            System.out.println("Waiting for client to connect...");
            return serverSocket.accept();
        }   
        catch(IOException e) {
            throw new Error("Unable to initialize communication");
        }    
    }
    
    /**
     * The communicator used to structure communication
     */
    private Communicator comm;
    /**
     * The packetizer used to deliver serialized data
     */
    private Packetizer packetizer;
    
    /**
     * Handle incoming packets (invoking robot functionality, obeying protocol) 
     */
    @Override public void handlePacket(IncomingPacket packet) {
        try {
            if(packetizer.is(packet,"drive")) {
                int left = packet.get_uint16("leftSpeed");
                int right = packet.get_uint16("rightSpeed");
                robotDrive(left,right);
            } else if(packetizer.is(packet,"stop")) {
                robotStop();
                comm.send(packetizer.makePacket("ack_stop"));
            } else if(packetizer.is(packet,"readBumpSensor")) {
                String sensor = packet.get_enum("sensor");
                comm.send(packetizer.makePacket("bumpSensorValue", readBumpSensor(sensor)));
            } else throw new Error("Should not happen");
        } catch(IOException exn) {
            throw new Error("IO Exception:"+exn);
        }
    }

    /**
     * Dummy drive operation implementation
     * @param left speed of left motor
     * @param right speed of right motor
     */
    protected void robotDrive(int left, int right) {
        System.out.println("Driving at speed "+left+","+right);
    }

    /**
     * Dummy stop operation 
     */
    protected void robotStop() {
        System.out.println("Stopped");
    }
    
    /**
     * Dummy bump sensor reading operation, always returns true
     * @param sensor the specification of what sensor to read
     * @return true
     */
    protected boolean readBumpSensor(String sensor) {
        System.out.println("Reading sensor "+sensor);
        return true;
    }
    
    /**
     * Simple setup that prepares to serve requests
     * @throws IOException if there was a communication problem
     */
    public void main() throws IOException {
        PacketFormat format = new MobileRobotComm.MRPacket();
        comm = new Communicator(format,getCommunicationChannel());
        packetizer = comm.getPacketizer();
        comm.addPacketHandler(this);
    }

    /**
     * Start main instance method
     */
    public static void main(String argv[]) throws IOException {
        new Driver().main(); 
    }
    
}
