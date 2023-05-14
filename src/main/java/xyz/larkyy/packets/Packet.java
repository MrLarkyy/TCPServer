package xyz.larkyy.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.larkyy.client.Client;
import xyz.larkyy.util.BByteBuf;
import xyz.larkyy.util.data.VarInt;

public class Packet {

    private final int id;
    private final ByteBuf data;

    protected Packet(int id, ByteBuf data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public ByteBuf getData() {
        return data;
    }

    public void write(ByteBuf buffer) {
        BByteBuf bbufer = BByteBuf.create(buffer);
        BByteBuf data = BByteBuf.create();

        data
                .writeVarInt(getId()) // Packet ID
                .writeBytes(this.data); // Packet Data

        bbufer
                .writeVarInt(data.getByteBuf().readableBytes())
                .writeBytes(data);
    }

    public static Packet read(ByteBuf byteBuf) {
        try {
            int length = VarInt.readVarInt(byteBuf);
            System.out.println("Length : "+length);
            ByteBuf data = byteBuf.readBytes(length);
            int packetId = VarInt.readVarInt(data);
            System.out.println("Packet id : "+packetId);

            return new Packet(packetId,data);
        } catch (Exception e) {
            return null;
        }
    }

    public void handle(Client client) {
        ByteBuf buffer = Unpooled.buffer();
        write(buffer);
        Packet packet = PacketDecoder.decode(client.getState(),buffer);
        if (packet != null) {
            packet.handle(client);
        }
    }
}
