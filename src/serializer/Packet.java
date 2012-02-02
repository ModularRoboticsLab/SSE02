package serializer;

import java.nio.ByteBuffer;

import serializer.PacketFormat.PacketModel;

/**
 * An abstract packet class, specialized into incoming or outgoing packets.
 * Stores various constants, the ID, the buffer used for storing bytes, and 
 * the specific packet format model that describes this packet
 * @author ups
 */
public abstract class Packet {
    /**
     * Magic number at the start of every packet
     */
    protected static final byte MAGIC = 87;
    /**
     * The size of the header: magic number, id, length (2 bytes)
     */
    protected static final int HEADER_SIZE = 1+1+2;
    /**
     * The packet format ID of this packet (must correspond to complete model)
     */
    protected byte id;
    /**
     * The bytes contained in this packet
     */
    protected ByteBuffer buffer;
    /**
     * The model describing this packet
     */
    protected PacketModel model;

    /**
     * Helper method: convert a signed byte into the bitwise corresponding unsigned value,
     * represented as an integer
     * @param b the byte to convert
     * @return the bitwise corresponding value
     */
    public static int unsignedByte(byte b) {
        return ((int)b)&0xff;  
    }
    
    /**
     * Get the ID of this packet, corresponding to the packet format model
     * @return packet ID
     */
    public int getID() {
        return unsignedByte(id);
    }

    /**
     * Get the byte buffer storing the bytes of this buffer
     * @return the byte buffer
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * Get all bytes stored in the local buffer
     * @return the bytes stored in the buffer
     */
    public byte[] getAllBytes() {
        return buffer.array();
    }

    /**
     * For debugging
     */
    public String toString() {
        if(buffer==null)
            return "Packet[NULL]";
        else {
            StringBuffer result = new StringBuffer("Packet[ ");
            byte[] bytes = buffer.array();
            for(int i=0; i<bytes.length; i++)
                result.append(unsignedByte(bytes[i])+" ");
            result.append("]");
            return result.toString();
        }
    }
}
