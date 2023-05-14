package xyz.larkyy.util.data;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class BufferUtil {

    public static String readString(ByteBuf buffer) {
        try {
            var stringLength = VarInt.readVarInt(buffer);
            byte[] data = new byte[stringLength];
            buffer.readBytes(data);
            return new String(data, StandardCharsets.UTF_8);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void writeString(String string, ByteBuf buffer) {
        // Length of the String
        VarInt.writeVarInt(string.length(),buffer);
        // Actual String
        buffer.writeBytes(string.getBytes(StandardCharsets.UTF_8));
    }

}
