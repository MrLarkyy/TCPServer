package xyz.larkyy.packets.status.serverbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.larkyy.client.Client;
import xyz.larkyy.packets.Packet;

public class ServerboundStatusPacket extends Packet {
    private static final int PACKET_ID = 0x00;
    protected ServerboundStatusPacket() {
        super(PACKET_ID, Unpooled.buffer());
    }

    public void handle(Client client) {
        client.handleStatus();
    }

    public static ServerboundStatusPacket read(ByteBuf buffer) {
        Packet readPacket = Packet.read(buffer);
        if (readPacket == null) {
            return null;
        }
        return read(readPacket);
    }

    public static ServerboundStatusPacket read(Packet packet) {
        if (packet.getId() != PACKET_ID) {
            return null;
        }
        return new ServerboundStatusPacket();
    }
}
