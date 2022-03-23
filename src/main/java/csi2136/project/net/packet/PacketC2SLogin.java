package csi2136.project.net.packet;

import csi2136.project.net.context.ServerContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.C2SMessage;
import csi2136.project.net.packet.message.Packet;

import java.io.IOException;
import java.nio.ByteOrder;

public class PacketC2SLogin extends Packet implements C2SMessage {

    private String username;
    private String password;

    public PacketC2SLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public PacketC2SLogin() {
    }

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
        buf.writeASCII(this.username, ByteOrder.BIG_ENDIAN);
        buf.writeASCII(this.password, ByteOrder.BIG_ENDIAN);
        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
        this.username = buf.readASCII(ByteOrder.BIG_ENDIAN);
        this.password = buf.readASCII(ByteOrder.BIG_ENDIAN);
        return this;
    }

    @Override
    public Packet onPacketReceived(ServerContext context) {
        //Verification
        return new PacketS2CLogin(this.username, true);
    }
}
