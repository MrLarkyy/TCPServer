package xyz.larkyy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class PacketServerHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        ByteBuf in = (ByteBuf) msg;

        try {
            while (in.isReadable()) {
                try {
                    final var length = readVarInt(in);
                    if (in.readableBytes() < length) {
                        return;
                    }
                    final var data = in.readBytes(length);
                    final var packetId = readVarInt(data);
                    System.out.println("Packet Length: "+length);
                    System.out.println("packetId: "+packetId);

                    if (packetId == 0x00) {
                        if (length == 1) {
                            var pktId = 0x01;
                            ByteBuf buffer = Unpooled.buffer();
                            ByteBuf packetData = Unpooled.buffer();
                            packetData.writeLong(10);

                            // Length
                            writeVarInt(packetData.readableBytes()*4+3,buffer);
                            // PacketID
                            writeVarInt(pktId,buffer);
                            // Data
                            buffer.writeBytes(packetData);

                            ctx.write(buffer);
                            ctx.flush();
                            System.out.println("Replying 2");
                            continue;
                        }

                        try {
                            final var v = readVarInt(data);
                            System.out.println("Protocol Version: "+v);
                        } catch (Exception ignored) {
                            continue;
                        }
                        var stringLength = readVarInt(data);
                        byte[] hostnameData = new byte[stringLength];
                        data.readBytes(hostnameData);
                        System.out.println("Hostname: "+ new String(hostnameData));

                        short port = data.readShort();
                        System.out.println("Port: "+port);

                        var nextState = readVarInt(data);
                        System.out.println("State: "+nextState);

                        ByteBuf buffer = Unpooled.buffer();

                        var pktId = 0x00;
                        String json = "{" +
                                "    \"version\": {" +
                                "        \"name\": \"1.19.4\"," +
                                "        \"protocol\": 762" +
                                "    }," +
                                "    \"players\": {" +
                                "        \"max\": 100," +
                                "        \"online\": 5," +
                                "        \"sample\": [" +
                                "            {" +
                                "                \"name\": \"thinkofdeath\"," +
                                "                \"id\": \"4566e69f-c907-48ee-8d71-d7ba5aa00d20\"" +
                                "            }" +
                                "        ]" +
                                "    }," +
                                "    \"description\": {" +
                                "        \"text\": \"Hello world\"" +
                                "    }," +
                                "    \"favicon\": \"data:image/png;base64,<data>\"," +
                                "    \"enforcesSecureChat\": true" +
                                "}";

                        ByteBuf packetData = Unpooled.buffer();
                        ByteBufUtil.writeAscii(packetData,json);

                        // Length
                        writeVarInt(packetData.readableBytes()*4+3,buffer);
                        // PacketID
                        writeVarInt(pktId,buffer);
                        // Data
                        buffer.writeBytes(packetData);


                        ctx.write(buffer);
                        ctx.flush();
                        System.out.println("Replying");


                    } else if (packetId == 0x01) {

                        System.out.println("PACKET 01");
                        System.out.flush();
                        /*
                        long ping = in.readLong();
                        System.out.println("Ping: "+ping);

                         */

                        ByteBuf packetData = Unpooled.buffer();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public int readVarInt(ByteBuf bb) throws Exception {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = bb.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public void writeVarInt(int value, ByteBuf bb) {
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
