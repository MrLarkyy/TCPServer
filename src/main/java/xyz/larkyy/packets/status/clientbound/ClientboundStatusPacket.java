package xyz.larkyy.packets.status.clientbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.larkyy.client.Client;
import xyz.larkyy.packets.Packet;
import xyz.larkyy.util.BByteBuf;

public class ClientboundStatusPacket extends Packet {

    private static final int PACKET_ID = 0x00;
    private final String json;
    public ClientboundStatusPacket(String json) {
        super(PACKET_ID, Unpooled.buffer());
        this.json = json;
    }

    public void write(ByteBuf buffer) {
        BByteBuf bbufer = BByteBuf.create(buffer);
        BByteBuf data = BByteBuf.create();

        data
                .writeVarInt(getId())
                .writeString(json);
        bbufer
                .writeVarInt(data.getByteBuf().readableBytes())
                .writeBytes(data);
    }

    public void handle(Client client) {
        client.sendPacket(this);
    }
}
