package xyz.larkyy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import xyz.larkyy.client.Client;
import xyz.larkyy.packets.Packet;
import xyz.larkyy.packets.PacketDecoder;

public class PacketServerHandler extends ChannelDuplexHandler {

    private final Client client;
    public PacketServerHandler(Client client) {
        this.client = client;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;

        try {
            while (in.isReadable()) {
                Packet packet = PacketDecoder.decode(client.getState(),in);
                System.out.println("Client state: "+client.getState().name());
                if (packet != null) {
                    System.out.println("Handle");
                    packet.handle(client);
                }

                /*
                try {
                    final var length = VarInt.readVarInt(in);
                    if (in.readableBytes() < length) {
                        return;
                    }
                    final var data = in.readBytes(length);
                    final var packetId = VarInt.readVarInt(data);
                    System.out.println("Packet Length: "+length);
                    System.out.println("packetId: "+packetId);

                    if (packetId == 0x00) {
                        if (length == 1) {
                            ByteBuf buffer = Unpooled.buffer();

                            var pktId = 0x00;
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

                            ByteBuf packetData = Unpooled.buffer();
                            // PacketID
                            VarInt.writeVarInt(pktId,packetData);
                            // Data
                            VarInt.writeVarInt(json.length(),packetData);
                            packetData.writeBytes(json.getBytes(StandardCharsets.UTF_8));

                            // Length
                            VarInt.writeVarInt(packetData.readableBytes(),buffer);
                            // Data
                            buffer.writeBytes(packetData);

                            ctx.write(buffer);
                            ctx.flush();
                            continue;
                        }

                        try {
                            final var v = VarInt.readVarInt(data);
                            System.out.println("Protocol Version: "+v);
                        } catch (Exception ignored) {
                            continue;
                        }
                        var stringLength = VarInt.readVarInt(data);
                        byte[] hostnameData = new byte[stringLength];
                        data.readBytes(hostnameData);
                        System.out.println("Hostname: "+ new String(hostnameData));

                        short port = data.readShort();
                        System.out.println("Port: "+port);

                        var nextState = VarInt.readVarInt(data);
                        System.out.println("State: "+nextState);

                    } else if (packetId == 0x01) {

                        System.out.println("PACKET 01");
                        System.out.flush();

                        long ping = data.readLong();
                        System.out.println("Ping: "+ping);

                        ByteBuf buffer = Unpooled.buffer();
                        ByteBuf packetData = Unpooled.buffer();
                        var pktId = 0x01;

                        // Packet ID
                        VarInt.writeVarInt(pktId,packetData);
                        // Data
                        packetData.writeLong(ping);
                        // Length
                        VarInt.writeVarInt(packetData.readableBytes(), buffer);
                        // Data
                        buffer.writeBytes(packetData);

                        ctx.write(buffer);
                        ctx.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 */
            }


        } finally {
            //ReferenceCountUtil.release(msg); // (2)
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
