package xyz.larkyy.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.larkyy.util.data.BufferUtil;
import xyz.larkyy.util.data.VarInt;

public class BByteBuf {

    private final ByteBuf byteBuf;

    private BByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public static BByteBuf create() {
        return new BByteBuf(Unpooled.buffer());
    }
    public static BByteBuf create(ByteBuf byteBuf) {
        return new BByteBuf(byteBuf);
    }

    public BByteBuf writeString(String string) {
        BufferUtil.writeString(string,byteBuf);
        return this;
    }

    public BByteBuf writeInt(int i) {
        byteBuf.writeInt(i);
        return this;
    }

    public BByteBuf writeShort(short s) {
        byteBuf.writeShort(s);
        return this;
    }

    public BByteBuf writeVarInt(int i) {
        VarInt.writeVarInt(i,byteBuf);
        return this;
    }

    public BByteBuf writeBytes(byte[] bytes) {
        byteBuf.writeBytes(bytes);
        return this;
    }
    public BByteBuf writeBytes(ByteBuf buffer) {
        byteBuf.writeBytes(buffer);
        return this;
    }
    public BByteBuf writeBytes(BByteBuf bbuffer) {
        return writeBytes(bbuffer.byteBuf);
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

}
