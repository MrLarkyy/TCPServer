package xyz.larkyy.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.larkyy.client.Client;
import xyz.larkyy.util.BByteBuf;
import xyz.larkyy.util.data.BufferUtil;
import xyz.larkyy.util.data.VarInt;

import java.nio.charset.StandardCharsets;

public class Handshake extends Packet {

    private static final int PACKET_ID = 0x00;
    private final int protocolVersion;
    private final String address;
    private final short port;
    private final State state;

    public Handshake(int protocolVersion, String address, short port, State state) {
        super(PACKET_ID, Unpooled.buffer());
        this.protocolVersion = protocolVersion;
        this.address= address;
        this.port = port;
        this.state = state;
    }

    public void write(ByteBuf buffer) {
        BByteBuf bbufer = BByteBuf.create(buffer);
        BByteBuf data = BByteBuf.create();

        data
                .writeVarInt(getId())
                .writeVarInt(protocolVersion)
                .writeString(address)
                .writeShort(port)
                .writeVarInt(state.getId());

        bbufer
                .writeVarInt(data.getByteBuf().readableBytes())
                .writeBytes(data);
    }

    public void handle(Client client) {
        client.handleHandshake(this);
    }

    public State getState() {
        return state;
    }

    public static int getPacketId() {
        return PACKET_ID;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public short getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public static Handshake read(ByteBuf buffer) {
        Packet readPacket = Packet.read(buffer);
        if (readPacket == null) {
            return null;
        }
        return read(readPacket);
    }

    public static Handshake read(Packet packet) {
        if (packet.getId() != PACKET_ID) {
            return null;
        }
        var data = packet.getData().copy();
        try {
            final var protocolVersion = VarInt.readVarInt(data); // Protocol Version
            String hostname = BufferUtil.readString(data); // Hostname
            short port = data.readShort(); // Port
            var nextState = VarInt.readVarInt(data); // State
            return new Handshake(protocolVersion,hostname,port,State.getState(nextState));
        } catch (Exception ignored) {
            return null;
        }
    }
}
