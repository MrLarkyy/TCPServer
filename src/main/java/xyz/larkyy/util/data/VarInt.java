package xyz.larkyy.util.data;

import io.netty.buffer.ByteBuf;

public class VarInt {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public static int readVarInt(ByteBuf bb) throws Exception {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            if (!bb.isReadable()) {
                throw new Exception();
            }
            currentByte = bb.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public static void writeVarInt(int value, ByteBuf bb) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                bb.writeByte(value);
                return;
            }

            bb.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

}
