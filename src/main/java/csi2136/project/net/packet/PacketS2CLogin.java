package csi2136.project.net.packet;

import csi2136.project.net.context.ClientContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.Packet;
import csi2136.project.net.packet.message.S2CMessage;
import csi2136.project.ui.client.ClientFrame;
import csi2136.project.ui.client.LoginScreen;

import java.io.IOException;
import java.nio.ByteOrder;

public class PacketS2CLogin extends Packet implements S2CMessage {

    protected String username;
    protected boolean success;

    public PacketS2CLogin() {

    }

    public PacketS2CLogin(String username, boolean success) {
        this.username = username;
        this.success = success;
    }

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
        buf.writeASCII(this.username, ByteOrder.BIG_ENDIAN);
        buf.writeBoolean(this.success);
        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
        this.username = buf.readASCII(ByteOrder.BIG_ENDIAN);
        this.success = buf.readBoolean();
        return this;
    }

    @Override
    public Packet onPacketReceived(ClientContext context) {
        ClientFrame frame = context.client.getFrame();
        LoginScreen panel = frame.getPanel(LoginScreen.class);

        if(this.success) {
            panel.error.setText("");
            frame.movePanel(2);
        } else {
            panel.error.setText("Invalid login credentials");
        }

        frame.repaint();
        return null;
    }

}
