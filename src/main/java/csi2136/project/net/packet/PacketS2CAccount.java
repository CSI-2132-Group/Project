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
import java.util.stream.Collectors;

public class PacketS2CAccount extends Packet implements S2CMessage {

	protected String username;
	protected List<User> users;
	protected List<Employee> employees;
	protected List<String> procedures;

	public PacketS2CAccount() {

	}

	public PacketS2CAccount(Database db, String username) {
	    try {
	    	this.username = username;

		    this.users = new ArrayList<>();
		    ResultSet result = db.send("SELECT * FROM User;");

		    while(result.next()) {
			    this.users.add(new User().read(result, db));
		    }

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
		buf.writeASCII(this.username, ByteOrder.BIG_ENDIAN);
	    buf.writeInt(this.users.size(), ByteOrder.BIG_ENDIAN);

	    for(User user : this.users) {
		    buf.writeObject(user);
	    }

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
		this.username = buf.readASCII(ByteOrder.BIG_ENDIAN);

		try {
			int size = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.users = new ArrayList<>();

			for(int i = 0; i < size; i++) {
				this.users.add(buf.readObject(new User()));
			}

			size = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.employees = new ArrayList<>();

			for(int i = 0; i < size; i++) {
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

        screen.user = this.users.stream().filter(user -> user.username.equals(this.username))
	        .findAny().orElse(null);

        screen.users = this.users;
	    screen.employees = this.employees;

	    screen.appointments = this.users.stream()
		    .flatMap(u -> u.patients.stream())
		    .flatMap(p -> p.appointments.stream())
		    .collect(Collectors.toList());

	    screen.procedures = this.procedures;
	    screen.updateTabs();
        return null;
    }

}
