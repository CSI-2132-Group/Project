package csi2136.project.net.packet;

import csi2136.project.core.Database;
import csi2136.project.net.context.ServerContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.C2SMessage;
import csi2136.project.net.packet.message.Packet;
import csi2136.project.ui.Utils;

import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PacketC2SLogin extends Packet implements C2SMessage {

    private String username;
    private String password;

    public PacketC2SLogin() {

    }

    public PacketC2SLogin(String username, String password) {
        this.username = username;
        this.password = password;
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
        Database db = context.server.getDatabase();

        try {
            ResultSet passwordValues = db.send(
                String.format("SELECT Password_Hash, Password_Salt from User WHERE '%s' = Username;", this.username));

            if(!passwordValues.next()) {
                return new PacketS2CLogin(this.username, false);
            }

            String tempHash = passwordValues.getString("Password_Hash");
            String tempSalt = passwordValues.getString("Password_Salt");

            String tempEnteredPass = this.password + tempSalt;
            tempEnteredPass = Utils.hash(tempEnteredPass);

            if(tempEnteredPass.equals(tempHash)) {
                context.listener.sendPacket(new PacketS2CAccount(db, this.username));
                return new PacketS2CLogin(this.username, true);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return new PacketS2CLogin(this.username, false);
    }

}
