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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import serializer.Communicator;
import serializer.IPacketHandler;
import serializer.IncomingPacket;
import serializer.PacketFormat;
import serializer.Packetizer;

/**
 * Simple controller that stores incoming packets in a queue,
 * and executes a fixed sequence of actions.
 * @author ups
 */
public class Controller implements IPacketHandler {

    /**
     * Queue for incoming packets
     */
    private LinkedList<IncomingPacket> delivery = new LinkedList<IncomingPacket>();
    
    /**
     * Create a socket for communication with the server (robot), on localhost port 4444
     * @return the newly created socketv
     */
    private static Socket getCommunicationChannel() { 
        try {
            return new Socket("localhost", 4444);
        } catch (UnknownHostException e) {
            throw new Error("Could not find host");
        } catch (IOException e) {
            throw new Error("Unable to create socket: "+e);
        } 
    }
    
    /**
     * Simple test routine
     */
    public static void main(String argv[]) throws IOException {
        new Controller().main();
    }
    
    /**
     * Hardcoded sequence of actions, for testing
     * @throws IOException if there was any problems with I/O
     */
    public void main() throws IOException {
        PacketFormat format = new MobileRobotComm.MRPacket();
        Communicator comm = new Communicator(format,getCommunicationChannel());
        comm.addPacketHandler(this);
        Packetizer ptz = comm.getPacketizer();
        comm.send(ptz.makePacket("drive",100,100));
        comm.send(ptz.makePacket("stop"));
        while(!ptz.is(receive(),"ack_stop")) { System.out.println("Waiting"); }
        comm.send(ptz.makePacket("drive",50,50));
    }

    /**
     * Blocking receive operation that reads packets from the queue, sleeping if no packets are available
     * @return a newly received packet
     */
    private IncomingPacket receive() {
        synchronized(delivery) {
            while(delivery.size()==0) {
                try {
                    delivery.wait();
                } catch (InterruptedException e) {
                    throw new Error("Should not happen");
                }
            }
            return delivery.removeFirst();
        }
    }

    /**
     * Handles delivey of incoming packets
     */
    @Override
    public void handlePacket(IncomingPacket packet) {
        synchronized(delivery) {
            delivery.add(packet);
            delivery.notifyAll();
        }
    }
    
}
