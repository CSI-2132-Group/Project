package csi2136.project.net.packet;

import csi2136.project.net.context.ClientContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.Packet;
import csi2136.project.net.packet.message.S2CMessage;

import java.io.IOException;
import java.nio.ByteOrder;

public class PacketS2CDisconnect extends Packet implements S2CMessage {

    private String reason;

    private PacketS2CDisconnect() {

    }

    public PacketS2CDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
        buf.writeASCII(this.reason, ByteOrder.BIG_ENDIAN);
        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
        this.reason = buf.readASCII(ByteOrder.BIG_ENDIAN);
        return this;
    }

    @Override
    public Packet onPacketReceived(ClientContext context) {
        //TODO: show error message here
        context.listener.disconnect(this.reason);
        return null;
    }

}
