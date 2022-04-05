package csi2136.project.net.packet;

import csi2136.project.core.Appointment;
import csi2136.project.core.Database;
import csi2136.project.core.Employee;
import csi2136.project.core.User;
import csi2136.project.net.context.ClientContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.Packet;
import csi2136.project.net.packet.message.S2CMessage;
import csi2136.project.ui.client.AccountScreen;
import csi2136.project.ui.client.ClientFrame;

import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacketS2CUser extends Packet implements S2CMessage {

    protected User user;
	protected List<Employee> employees;
	protected List<String> procedures;

	public PacketS2CUser() {

	}

	public PacketS2CUser(Database db, String username) {
	    try {
		    User user = new User();
		    ResultSet result = db.send(String.format("SELECT * FROM User WHERE '%s' = Username;", username));

	        if(result.next()) {
                user = new User().read(result, db);
            }

		    this.user = user;

		    this.employees = new ArrayList<>();
		    result = db.send("SELECT * FROM Employee;");

		    while(result.next()) {
			    this.employees.add(new Employee().read(result, db));
		    }

		    this.procedures = new ArrayList<>();
		    result = db.send("SELECT * FROM Procedures;");

		    while(result.next()) {
			    this.procedures.add(new Appointment.Procedure().read(result, db).type);
		    }
        } catch(SQLException e) {
	        e.printStackTrace();
        }
	}

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
	    buf.writeObject(this.user);

	    buf.writeInt(this.employees.size(), ByteOrder.BIG_ENDIAN);

	    for(Employee employee : this.employees) {
		    buf.writeObject(employee);
	    }

	    buf.writeInt(this.procedures.size(), ByteOrder.BIG_ENDIAN);

	    for(String procedure : this.procedures) {
		    buf.writeASCII(procedure, ByteOrder.BIG_ENDIAN);
	    }

	    return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
		this.user = buf.readObject(new User());

		try {
			int size = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.employees = new ArrayList<>();

			for(int i = 0; i < size; i++) {
				System.out.println(i);
				this.employees.add(buf.readObject(new Employee()));
			}

			size = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.procedures = new ArrayList<>();

			for(int i = 0; i < size; i++) {
				this.procedures.add(buf.readASCII(ByteOrder.BIG_ENDIAN));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

        return this;
    }

    @Override
    public Packet onPacketReceived(ClientContext context) {
        ClientFrame frame = context.client.getFrame();
        AccountScreen screen = frame.getPanel(AccountScreen.class);
	    screen.user = this.user;
	    screen.employees = employees;
	    screen.procedures = procedures;
	    screen.updateTabs();
        return null;
    }

}
