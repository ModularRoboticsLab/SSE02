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

import serializer.PacketFormat.BooleanFieldModel;
import serializer.PacketFormat.EnumFieldModel;
import serializer.PacketFormat.FieldModel;
import serializer.PacketFormat.IntFieldModel;
import serializer.PacketFormat.PacketModel;

/**
 * Helper object for packing structured data into byte serialized format according to a
 * given packet model, and utility methods for testing contents of a packet before
 * deserialization. 
 * @author ups
 */
public class Packetizer {

    /**
     * The complete packet format model used by this packetizer
     */
    private List<PacketModel> model;
    
    /**
     * Create packetizer according to given model
     * @param model the model to use for this packetizer
     */
    public Packetizer(List<PacketModel> model) {
        this.model = model;
    }

    /**
     * Create an outgoing packet according to the supplied packet format name (must be
     * found in model) and data
     * @param name the name of the specific packet format to use
     * @param arguments the data to store in the packet, must correspond to format
     * @return the serialized packet
     */
    public OutgoingPacket makePacket(String name, Object ... arguments) {
        PacketModel selected = findModel(name);
        OutgoingPacket result = new OutgoingPacket(model.indexOf(selected),selected);
        int length = selected.fields.size();
        if(arguments.length!=length) throw new Error("Data mismatch");
        for(int i=0; i<length; i++)
            encode(selected.fields.get(i),arguments[i],result.getBuffer());
        return result;
    }

    /**
     * Encode data into buffer according to the format of the field
     * @param field the field specifying the data format
     * @param argument the data to store
     * @param buffer the buffer to store the data in
     */
    private void encode(final FieldModel field, final Object argument, final ByteBuffer buffer) {
        field.dispatch(new IFieldModelDispatch() {
            @Override public void dispatchBooleanField(BooleanFieldModel model) {
                Boolean value = (Boolean)argument;
                buffer.put(value.booleanValue() ? (byte)1 : (byte)0); 
            }
            @Override
            public void dispatchEnumField(EnumFieldModel model) {
                String value = (String)argument;
                buffer.put((byte)model.getNames().indexOf(value));
            }
            @Override
            public void dispatchIntField(IntFieldModel model) {
                int value = (Integer)argument;
                switch(model.size) {
                case 8: buffer.put((byte)value); break;
                case 16: buffer.putShort((short)value); break;
                case 32: buffer.putInt(value); break;
                default: throw new Error("Bitsize not supported: "+model.size);
                }
            }
            
        });
    }

    /**
     * Lookup a specific packet model by name
     * @param name the name of the packet model
     * @return the specific model
     */
    private PacketModel findModel(String name) {
        for(PacketModel p: model)
            if(p.getName().equals(name)) return p;
        throw new Error("Packet model not found: "+name);
    }

    /**
     * Test if a given packet is of a given format, according to the packetizer model
     * @param packet the packet to inspect
     * @param name the name of the packet format
     * @return true if it is of the format, false otherwise
     */
    public boolean is(Packet packet, String name) {
        PacketModel selected = findModel(name);
        return packet.getID()==model.indexOf(selected);
    }
    
}
