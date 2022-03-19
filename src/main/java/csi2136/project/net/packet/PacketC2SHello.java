package csi2136.project.net.packet;

import csi2136.project.net.context.ServerContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.C2SMessage;
import csi2136.project.net.packet.message.Packet;
import csi2136.project.net.util.PacketRegistry;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class PacketC2SHello extends Packet implements C2SMessage {

    protected List<String> packetRegistry;

	public PacketC2SHello() {
        this.packetRegistry = PacketRegistry.getRegistry();
	}

    public List<String> getPacketRegistry() {
        return this.packetRegistry;
    }

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
	    buf.writeByte((byte)this.packetRegistry.size());

        for(String s : this.packetRegistry) {
            buf.writeASCII(s, ByteOrder.BIG_ENDIAN);
        }

        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
        this.packetRegistry = new ArrayList<>();
        int size = buf.readUnsignedByte();

        for(int i = 0; i < size; i++) {
            this.packetRegistry.add(buf.readASCII(ByteOrder.BIG_ENDIAN));
        }

        return this;
    }

    @Override
    public Packet onPacketReceived(ServerContext context) {
        if(!PacketRegistry.getRegistry().equals(this.getPacketRegistry())) {
            context.listener.schedule((Runnable)context.listener::disconnect);
            return new PacketS2CDisconnect("Mismatching Packet Registry! Outdated Client?");
        }

        return null;
    }

}
