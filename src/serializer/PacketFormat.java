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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A packet format specification language for driving the corresponding serialization
 * (and hence communication) classes.  Create a packet model by overriding the build 
 * method.
 * @author ups
 */
public abstract class PacketFormat {
    
    /**
     * Metamodel for a single packet which has a name and a number of fields
     */
    protected static class PacketModel {
        /**
         * Name of the packet
         */
        private String packetName;
        /**
         * Field of the packet
         */
        List<FieldModel> fields = new ArrayList<FieldModel>();
        /**
         * Accumulated size of the fields, in bytes.  When -1 indicates that the offsets stored
         * in each of the fields also is -1 and hence invalid.
         */
        int fieldsSize = -1;
        /**
         * Create a packet format model with the corresponding name
         * @param name the name of the packet format
         */
        public PacketModel(String name) { this.packetName = name; }
        /**
         * Add an integer format field to the packet
         * @param name of the field
         * @param unsigned whether the field is unsigned (if true) or signed (if false)
         * @param size in bits
         */
        public void add_int(String name, boolean unsigned, int size) {
            fields.add(new IntFieldModel(name,unsigned,size));
        }
        /**
         * Add an enum field to the packet
         * @param name of the field
         * @param enames names of the enum elements
         */
        public void add_enum(String name, String[] enames) {
            fields.add(new EnumFieldModel(name,enames));
        }
        /**
         * Add a boolean field to the packet
         * @param name of the field
         */
        public void add_boolean(String name) {
            fields.add(new BooleanFieldModel(name));
        }
        /**
         * Compute the individual offsets (in bytes) in each of the field
         */
        private void computeOffsets() {
            if(fieldsSize>-1) return; // already computed
            final int[] offset = new int[1]; // "mutable integer object", incremented in loop
            for(FieldModel f: fields) f.dispatch(new IFieldModelDispatch() {
                @Override public void dispatchBooleanField(BooleanFieldModel model) {
                    model.setOffset(offset[0]++);
                }
                @Override public void dispatchEnumField(EnumFieldModel model) {
                    model.setOffset(offset[0]++);
                }
                @Override public void dispatchIntField(IntFieldModel model) {
                    model.setOffset(offset[0]);
                    offset[0]+=model.size/8;
                }});
            fieldsSize = offset[0];
        }
        /**
         * Get total storage occupied by the fields
         * @return total storage in bytes
         */
        public int getFieldStorage() {
            computeOffsets(); return fieldsSize;
        }
        /**
         * Get the specific offset of a given field
         * @param fieldName the name of the field
         * @return offset in bytes
         */
        public int fieldOffset(String fieldName) {
            computeOffsets();
           FieldModel model = getField(fieldName);
           if(model==null) throw new Error("Illegal field name: "+fieldName);
           if(model.offset==-1) throw new Error("Field index not initialized: "+fieldName);
           return model.offset;
        }
        /**
         * Get the model of the corresponding the field
         * @param fieldName the name of the field
         * @return the field model
         */
        public FieldModel getField(String fieldName) {
            for(FieldModel f: fields) if(fieldName.equals(f.name)) return f;
            throw new Error("Field not found: "+fieldName);
            
        }
        /**
         * Get the name of the packet format
         * @return packet format name
         */
        public String getName() { return packetName; }
    }
    /**
     * Metamodel for a single field of a packet, abstract, specialized for specific types
     */
    protected static abstract class FieldModel { 
        /**
         * Name of the field
         */
        private String name;
        /**
         * Offset (in bytes) of the field, relative to packet start
         */
        private int offset = -1;
        /**
         * Create field with corresponding name
         * @param name of the field
         */
        public FieldModel(String name) { this.name = name; }
        /**
         * Dispatch to a target object according to subtype
         * @param target to dispatch to
         */
        public abstract void dispatch(IFieldModelDispatch target);
        /**
         * Set the offset of the field
         * @param offset the ofset (in bytes) of the field, relative to packet start
         */
        public void setOffset(int offset) { this.offset = offset; }
    }
    /**
     * Metamodel for a packet field holding an integer value
     */
    protected static class IntFieldModel extends FieldModel {
        /**
         * Whether the integer is unsigned
         */
        private boolean unsigned;
        /**
         * The size of the integer, in bits
         */
        int size;
        /**
         * Create an integer field model
         * @param name name of the field
         * @param unsigned whether the integer value is unsigned
         * @param size the size in bits of the integer value
         */
        public IntFieldModel(String name, boolean unsigned, int size) {
            super(name);
            this.unsigned = unsigned; this.size = size;
        }
        /**
         * Dispatcher
         */
        @Override public void dispatch(IFieldModelDispatch target) {
            target.dispatchIntField(this);
        }
    }
    /**
     * Metamodel for a packet field holding an enum value
     */
    protected static class EnumFieldModel extends FieldModel {
        /**
         * The textual representation of the enum values
         */
        private List<String> names = new ArrayList<String>();
        /**
         * Create enum field model
         * @param name name of the field
         * @param enames textual names of the enum values
         */
        public EnumFieldModel(String name, String[] enames) {
            super(name);
            names.addAll(Arrays.asList(enames));
        }
        /**
         * Dispatcher
         */
        @Override public void dispatch(IFieldModelDispatch target) {
            target.dispatchEnumField(this);
        }
        /**
         * Get enum names
         * @return textual representations of the enum value names
         */
        public List<String> getNames() { return names; }
    }
    /**
     * Metamodel for a packet field hodling a boolean value
     */
    protected static class BooleanFieldModel extends FieldModel {
        /**
         * Create boolean field of the given name
         * @param name of the field
         */
        public BooleanFieldModel(String name) { super(name); }
        @Override public void dispatch(IFieldModelDispatch target) {
            target.dispatchBooleanField(this);
        }
    }
    
    /**
     * The complete metamodel describing all packets of this format
     */
    private List<PacketModel> completeModel = new ArrayList<PacketModel>();
    /**
     * The current packet format being constructed
     */
    private PacketModel current;
    
    /**
     * Create a new packet format by building the packet format model
     */
    public PacketFormat() {
        build();
        completeModel.add(current);
    }
    /**
     * Start the definition of a new packet
     * @param name the name of the packet
     * @return the builder instance
     */
    public PacketFormat packet(String name) {
        if(current!=null) completeModel.add(current);
        current = new PacketModel(name);
        return this; 
    }
    /**
     * Add an unsigned 16-bit integer field to the current packet
     * @param name the name of the field
     * @return the builder instance
     */
    public PacketFormat _uint16(String name) {
        current.add_int(name,true,16);
        return this; 
    }
    /**
     * Add an enum field to the current packet
     * @param name the name of the field
     * @param enames the textual names of the enum values
     * @return the builder instance
     */
    public PacketFormat _enum(String name, String ... enames) {
        current.add_enum(name,enames);
        return this; 
    }
    /**
     * Add a boolean field to the current packet
     * @param name the name of the field
     * @return the builder instance
     */
    public PacketFormat _boolean(String name) {
        current.add_boolean(name);
        return this; 
    }

    /**
     * Get the complete packet format model
     * @return the model instance
     */
    public List<PacketModel> getModel() { return completeModel; }
    
    /**
     * Build the model instance
     */
    protected abstract void build();
}
