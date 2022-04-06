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

	public PacketC2SEmployees() {

	}

	public PacketC2SEmployees(List<Employee> employees) {
	    this.employees = employees;
	}

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
	    buf.writeInt(this.employees.size(), ByteOrder.BIG_ENDIAN);

	    for(Employee employee : this.employees) {
		    buf.writeObject(employee);
	    }

        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
		int size = buf.readInt(ByteOrder.BIG_ENDIAN);
		this.employees = new ArrayList<>();

	    for(int i = 0; i < size; i++) {
		    this.employees.add(buf.readObject(new Employee()));
	    }

        return this;
    }

	@Override
	public Packet onPacketReceived(ServerContext context) {
		try {
			for(Employee employee : this.employees) {
				employee.write(context.server.getDatabase());
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
