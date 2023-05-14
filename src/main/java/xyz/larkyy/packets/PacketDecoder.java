package xyz.larkyy.packets;

import io.netty.buffer.ByteBuf;
import xyz.larkyy.packets.status.serverbound.ServerboundStatusPacket;

public class PacketDecoder {

    public static Packet decode(State state, ByteBuf byteBuf) {
        Packet p = Packet.read(byteBuf);
        if (p == null) {
            return null;
        }
        switch (state) {
            case HANDSHAKING -> {
                if (p.getId() == 0x00) {
                    return Handshake.read(p);
                }
            }
            case STATUS -> {
                if (p.getId() == 0x00) {
                    return ServerboundStatusPacket.read(p);
                }
            }
            case PLAY -> {

            }
            case LOGIN -> {

            }
        }
        return null;
    }

}
