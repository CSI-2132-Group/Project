package csi2136.project.net.packet;

import csi2136.project.core.User;
import csi2136.project.net.context.ServerContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.C2SMessage;
import csi2136.project.net.packet.message.Packet;

import java.io.IOException;
import java.sql.SQLException;

public class PacketC2SUser extends Packet implements C2SMessage {

    protected User user;

	public PacketC2SUser() {

	}

	public PacketC2SUser(User user) {
	    this.user = user;
	}

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
	    buf.writeObject(this.user);
        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
		this.user = buf.readObject(new User());
        return this;
    }

	@Override
	public Packet onPacketReceived(ServerContext context) {
		try {
			this.user.write(context.server.getDatabase());
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return new PacketS2CUser(context.server.getDatabase(), this.user.username);
	}

}
