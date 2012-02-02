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

package serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A generic communicator that for a given packet format and socket provides the ability
 * to send and receiver arbitrary packets using this format.  Packet reception handled as
 * an observer, serialization through Packetizer objects for which this object acts as a
 * factory.  
 * 
 * Packet reception runs in a separate thread (but dispatches all packets in that
 * same thread), packet sending runs in the callers thread.
 * 
 * WARNING: makes strong assumptions about how data is delivered, it assumes that a single
 * read command provides a single packet, nothing more, nothing less!
 * 
 * @author ups
 */
public class Communicator extends Thread {
    
    /**
     * Store incoming packets in this buffer, if too large the communicator will fail
     * in its current implementation
     */
    private static final int MAX_BUFFER_SIZE = 1024;
    
    /**
     * The packet format (aka model) used for this communicator
     */
    private PacketFormat format;
    /**
     * The set of packet handlers (observers)
     */
    private Set<IPacketHandler> handlers = Collections.synchronizedSet(new HashSet<IPacketHandler>());
    /**
     * The bidirectional channel on which communication is done
     */
    private Socket channel;
    /**
     * The output stream obtained from the channel
     */
    private OutputStream outs;
    /**
     * The input stream obtained from the channel
     */
    private InputStream ins;
    
    /**
     * Create a new communicator object with the given packet format working over the given socket,
     * and also starts the packet reception thread.
     * @param format the packet format to use
     * @param communicationChannel the socket to use
     * @throws IOException if input/output streams could not be created
     */
    public Communicator(PacketFormat format, Socket communicationChannel) throws IOException {
        this.format = format;
        this.channel = communicationChannel;
        this.outs = channel.getOutputStream();
        this.ins = channel.getInputStream();
        this.start();
    }
    /**
     * Add a packet handler (observer for packet reception)
     * @param handler the handler, invoked when a new packet arrives
     */
    public void addPacketHandler(IPacketHandler handler) {
        handlers.add(handler);
    }
    /**
     * Create a new packetizer object with the packet format of this communicator
     * @return a new packetizer using the packet format of this communicator
     */
    public Packetizer getPacketizer() {
        return new Packetizer(format.getModel());
    }
    /**
     * Send an outgoing packet using this communicator
     * @param packet the packet to send
     * @throws IOException if the packet could not be sent
     */
    public void send(OutgoingPacket packet) throws IOException {
        byte[] bytes = packet.getAllBytes();
        outs.write(bytes);
        outs.flush();
    }
    /**
     * The behavior of the packet reception thread
     * WARNING: makes strong assumptions about how data is delivered, it assumes that a single
     * read command provides a single packet, nothing more, nothing less!
     */
    @Override
    public void run() {
        try {
            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            while(true) {
                // Read data
                if(ins.read(buffer)==-1) break; // EOF
                // Create packet from data and distribute to observers
                IncomingPacket p = new IncomingPacket(buffer,format.getModel());
                for(IPacketHandler h: handlers) {
                    h.handlePacket(p);
                }
            }
            System.out.println("Communication channel closed.");
        } catch (IOException e) {
            throw new Error("IO Exception: "+e);
        }
    }
}
