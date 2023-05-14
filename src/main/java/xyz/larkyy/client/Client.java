package xyz.larkyy.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import xyz.larkyy.PacketServerHandler;
import xyz.larkyy.packets.Handshake;
import xyz.larkyy.packets.Packet;
import xyz.larkyy.packets.State;
import xyz.larkyy.packets.status.clientbound.ClientboundStatusPacket;

public class Client {

    private State state = State.HANDSHAKING;
    private final Channel channel;

    public Client(Channel channel) {
        this.channel = channel;
        channel.pipeline().addLast(new PacketServerHandler(this));
    }

    public State getState() {
        return state;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void handleHandshake(Handshake handshake) {
        System.out.println("Handling Handshake");
        if (handshake == null) {
            return;
        }
        if (handshake.getState() == null) {
            setState(State.STATUS);
            return;
        }
        switch (handshake.getState()) {
            case HANDSHAKING -> {
                setState(State.HANDSHAKING);
            }
            case STATUS -> {
                setState(State.STATUS);
            }
            case LOGIN -> {
                setState(State.LOGIN);
            }
            default -> {
                setState(State.PLAY);
            }
        }
    }

    public void handleStatus() {
        String json = "{" +
                "    \"version\": {" +
                "        \"name\": \"\\u00a7dYOU REALLY ARE\"," +
                "        \"protocol\": 762" +
                "    }," +
                "    \"players\": {" +
                "        \"max\": 420," +
                "        \"online\": 69," +
                "        \"sample\": [" +
                "            {" +
                "                \"name\": \"U ARE\"," +
                "                \"id\": \"4566e69f-c907-48ee-8d71-d7ba5aa00d20\"" +
                "            },"+
                "            {" +
                "                \"name\": \"GAY!\"," +
                "                \"id\": \"4566e69f-c907-48ee-8d71-d7ba5aa00d20\"" +
                "            }"+
                "        ]" +
                "    }," +
                "    \"description\": {" +
                "        \"text\": \"\\u00a7dUR GAY LOL HAHAHAH\"" +
                "    }," +
                "    \"enforcesSecureChat\": true" +
                "}";
        Packet packet = new ClientboundStatusPacket(json);
        System.out.println("Sending status packet");
        sendPacket(packet);
    }

    public void sendPacket(Packet packet) {
        ByteBuf buffer = Unpooled.buffer();
        packet.write(buffer);
        System.out.println("Packet sent");
        channel.pipeline().write(buffer);
        channel.pipeline().flush();
    }
}
