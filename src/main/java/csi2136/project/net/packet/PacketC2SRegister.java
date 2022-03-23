package csi2136.project.net.packet;

import csi2136.project.AccountType;
import csi2136.project.DBHandler;
import csi2136.project.net.context.ServerContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.C2SMessage;
import csi2136.project.net.packet.message.Packet;

import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PacketC2SRegister extends Packet implements C2SMessage {

    private String username;
    private String password;
    private String insurance;
    private AccountType type;

    public PacketC2SRegister(String username, String password, String insurance, AccountType type) {
        this.username = username;
        this.password = password;
        this.insurance = insurance;
        this.type = type;
    }

    public PacketC2SRegister() {
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
        //Verification
        try {
            ResultSet amount = DBHandler.getInst().getQuery("SELECT COUNT(*) FROM user WHERE Username ='" + this.username + "'");

            if(amount.getInt(0) > 0) {
                //Write data to database
                String salt = generateSalt();
                String hashedPass = hash(this.password + salt);
                DBHandler.getInst().getQuery("INSERT INTO user VALUES" +
                                "('"+ this.username + "', '" + hashedPass +"' , '"+ salt + "' , '" + this.insurance + "')");


                //upon success re-use S2C login
                return new PacketS2CLogin(this.username, true);

            }
            //Create new packet
            //Print Error


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PacketS2CLogin(this.username, false);
    }

    public String generateSalt(){
        return "";
    }
    public String hash(String value){

        return value;
    }
}