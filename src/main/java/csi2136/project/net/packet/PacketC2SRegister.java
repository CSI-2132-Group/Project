package csi2136.project.net.packet;

import csi2136.project.AccountType;
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

public class PacketC2SRegister extends Packet implements C2SMessage {

    private String username;
    private String password;
    private String insurance;
    private AccountType type;

    public PacketC2SRegister() {

    }

    public PacketC2SRegister(String username, String password, String insurance, AccountType type) {
        this.username = username;
        this.password = password;
        this.insurance = insurance;
        this.type = type;
    }

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
        buf.writeASCII(this.username, ByteOrder.BIG_ENDIAN);
        buf.writeASCII(this.password, ByteOrder.BIG_ENDIAN);
        buf.writeASCII(this.insurance, ByteOrder.BIG_ENDIAN);
        buf.writeInt(this.type.ordinal(), ByteOrder.BIG_ENDIAN);
        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
        this.username = buf.readASCII(ByteOrder.BIG_ENDIAN);
        this.password = buf.readASCII(ByteOrder.BIG_ENDIAN);
        this.insurance = buf.readASCII(ByteOrder.BIG_ENDIAN);
        this.type = AccountType.values()[buf.readInt(ByteOrder.BIG_ENDIAN)];
        return this;
    }

    @Override
    public Packet onPacketReceived(ServerContext context) {
        if(this.username.isEmpty()) {
            return PacketS2CRegister.ofFailure(this.username, "Invalid username");
        } else if(this.password.isEmpty()) {
            return PacketS2CRegister.ofFailure(this.password, "Invalid password");
        }

        Database db = context.server.getDatabase();

        try {
            ResultSet amount = db.send(String.format("SELECT COUNT(*) FROM user WHERE Username = '%s';", this.username));
            amount.next();

            if(amount.getInt(1) > 0) {
                return PacketS2CRegister.ofFailure(this.username, "Username already in use");
            }

            String salt = Utils.generateSalt();
            String hashedPass = Utils.hash(this.password + salt);
            db.send(String.format("INSERT INTO user VALUES('%s', NULL, '%s', '%s', '%s');", this.username, hashedPass, salt, this.insurance));
            context.listener.sendPacket(new PacketS2CUser(db, this.username));
            return PacketS2CRegister.ofSuccess(this.username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return PacketS2CRegister.ofFailure(this.username, "Unexpected error");
    }

}