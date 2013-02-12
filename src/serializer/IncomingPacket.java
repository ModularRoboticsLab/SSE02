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

import java.nio.ByteBuffer;
import java.util.List;

import serializer.PacketFormat.EnumFieldModel;
import serializer.PacketFormat.PacketModel;

/**
 * An incoming packet: a Packet specialized for reading data from a byte
 * buffer.  First verifies that the packet makes sense and then make the
 * data available on a field-name basis according to the packet format model. 
 * @author ups
 */
public class IncomingPacket extends Packet {

    /**
     * Create a new incoming packet using the given bytes and complete set of packet formats.
     * The packet ID value is an index into the list of packet models, an error is signaled
     * if the ID exceeds the size of the list.
     * @param bytes the raw incoming bytes (excess bytes will be ignored)
     * @param completeModel the set of models, one of which must describe this packet
     */
    public IncomingPacket(byte[] bytes, List<PacketModel> completeModel) {
        ByteBuffer initialBuffer = ByteBuffer.wrap(bytes);
        if(initialBuffer.get()!=MAGIC) throw new Error("Illegal packet");
        id = initialBuffer.get();
        if(id>=completeModel.size()) throw new Error("Illegal packet ID");
        short len = initialBuffer.getShort();
        byte[] realBuffer = new byte[HEADER_SIZE+len];
        System.arraycopy(bytes, 0, realBuffer, 0, realBuffer.length);
        buffer = ByteBuffer.wrap(realBuffer);
        this.model = completeModel.get(id);
    }

    /**
     * Get an unsigned 16-bit integer of the given name (must be described by packet format)
     * @param fieldName the name identifying the data segment
     * @return the 16-bit unsigned integer stored at that data segment
     */
    public int get_uint16(String fieldName) {
        return ((int)buffer.getShort(HEADER_SIZE+model.fieldOffset(fieldName)))&0xffff;
    }
    
    /**
     * Get a boolean value of the given name (must be described by the packet format)
     * @param fieldName the name identifying the data segment
     * @return the boolean stored at that data segment
     */
    public boolean get_boolean(String fieldName) {
    	return buffer.get(HEADER_SIZE+model.fieldOffset(fieldName))!=0;
    }

    /**
     * The textual representation of the enum value of the given name (must be described by packet format)
     * @param fieldName the name identifying the data segment
     * @return the textual representation of the enum value stored at that data segment
     */
    public String get_enum(String fieldName) {
        byte value = buffer.get(HEADER_SIZE+model.fieldOffset(fieldName));
        EnumFieldModel m = (EnumFieldModel)model.getField(fieldName);
        return m.getNames().get(((int)value)&0xff);
    }

}
