package csi2136.project.net.packet;

import csi2136.project.core.Employee;
import csi2136.project.net.context.ServerContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.C2SMessage;
import csi2136.project.net.packet.message.Packet;

import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacketC2SEmployees extends Packet implements C2SMessage {

    protected List<Employee> employees;
	protected List<Integer> removed;

	public PacketC2SEmployees() {

	}

	public PacketC2SEmployees(List<Employee> employees, List<Integer> removed) {
	    this.employees = employees;
		this.removed = removed;
	}

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
	    buf.writeInt(this.employees.size(), ByteOrder.BIG_ENDIAN);

	    for(Employee employee : this.employees) {
		    buf.writeObject(employee);
	    }

	    buf.writeInt(this.removed.size(), ByteOrder.BIG_ENDIAN);

	    for(Integer id : this.removed) {
		    buf.writeInt(id, ByteOrder.BIG_ENDIAN);
	    }

        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
		int size = buf.readInt(ByteOrder.BIG_ENDIAN);
		this.employees = new ArrayList<>();
	    this.removed = new ArrayList<>();

	    for(int i = 0; i < size; i++) {
		    this.employees.add(buf.readObject(new Employee()));
	    }

	    size = buf.readInt(ByteOrder.BIG_ENDIAN);

	    for(int i = 0; i < size; i++) {
		    this.removed.add(buf.readInt(ByteOrder.BIG_ENDIAN));
	    }

        return this;
    }

	@Override
	public Packet onPacketReceived(ServerContext context) {
		try {
			for(Integer id : this.removed) {
				context.server.getDatabase().send(String.format("DELETE FROM Employee WHERE %d = Employee_ID;", id));
			}

			for(Employee employee : this.employees) {
				employee.write(context.server.getDatabase());
			}

			context.serverListener.sendPacketToAllExcept(new PacketS2CAccount(context.server.getDatabase(), null), context.listener);
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
