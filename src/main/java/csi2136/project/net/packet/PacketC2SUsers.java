package csi2136.project.net.packet;

import csi2136.project.core.User;
import csi2136.project.net.context.ServerContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.C2SMessage;
import csi2136.project.net.packet.message.Packet;

import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacketC2SUsers extends Packet implements C2SMessage {

	protected List<User> users;
	protected List<String> removed;

	public PacketC2SUsers() {

	}

	public PacketC2SUsers(List<User> users, List<String> removed) {
	    this.users = users;
	    this.removed = removed;
	}

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
	    buf.writeInt(this.users.size(), ByteOrder.BIG_ENDIAN);

	    for(User user : this.users) {
		    buf.writeObject(user);
	    }

	    buf.writeInt(this.removed.size(), ByteOrder.BIG_ENDIAN);

	    for(String s : this.removed) {
		    buf.writeASCII(s, ByteOrder.BIG_ENDIAN);
	    }

        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
		int size = buf.readInt(ByteOrder.BIG_ENDIAN);
		this.users = new ArrayList<>();
	    this.removed = new ArrayList<>();

	    for(int i = 0; i < size; i++) {
		    this.users.add(buf.readObject(new User()));
	    }

	    size = buf.readInt(ByteOrder.BIG_ENDIAN);

	    for(int i = 0; i < size; i++) {
		    this.removed.add(buf.readASCII(ByteOrder.BIG_ENDIAN));
	    }

        return this;
    }

	@Override
	public Packet onPacketReceived(ServerContext context) {
		try {
			for(String id : this.removed) {
				context.server.getDatabase().send(String.format("DELETE FROM User WHERE '%s' = Username;", id));
			}

			for(User user : this.users) {
				user.write(context.server.getDatabase());
			}

			context.serverListener.sendPacketToAllExcept(new PacketS2CAccount(context.server.getDatabase(), null), context.listener);
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
