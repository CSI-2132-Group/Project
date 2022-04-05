package csi2136.project.net.packet;

import csi2136.project.net.context.ClientContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.Packet;
import csi2136.project.net.packet.message.S2CMessage;
import csi2136.project.ui.client.ClientFrame;
import csi2136.project.ui.client.RegisterScreen;

import java.io.IOException;
import java.nio.ByteOrder;

public class PacketS2CRegister extends Packet implements S2CMessage {

    protected String username;
    protected boolean success;
    protected String error;

    public PacketS2CRegister() {

    }

    public PacketS2CRegister(String username, boolean success, String error) {
        this.username = username;
        this.success = success;
        this.error = error;
    }

    public static PacketS2CRegister ofSuccess(String username) {
        return new PacketS2CRegister(username, true, null);
    }

    public static PacketS2CRegister ofFailure(String username, String error) {
        return new PacketS2CRegister(username, false, error);
    }

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
        buf.writeASCII(this.username, ByteOrder.BIG_ENDIAN);
        buf.writeBoolean(this.success);

        if(!this.success) {
            buf.writeASCII(this.error, ByteOrder.BIG_ENDIAN);
        }

        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
        this.username = buf.readASCII(ByteOrder.BIG_ENDIAN);
        this.success = buf.readBoolean();

        if(!this.success) {
            this.error = buf.readASCII(ByteOrder.BIG_ENDIAN);
        }

        return this;
    }

    @Override
    public Packet onPacketReceived(ClientContext context) {
        ClientFrame frame = context.client.getFrame();
        RegisterScreen panel = frame.getPanel(RegisterScreen.class);

        if(this.success) {
            panel.error.setText("");
            frame.movePanel(1);
        } else {
            panel.error.setText(this.error);
        }

        frame.repaint();
        return null;
    }

}
