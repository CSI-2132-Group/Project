package csi2136.project.net.packet;

import csi2136.project.core.Appointment;
import csi2136.project.core.Patient;
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
import java.util.stream.Collectors;

public class PacketC2SAppointments extends Packet implements C2SMessage {

    protected List<Appointment> appointments;
    protected List<String> usernames;
	protected List<Integer> patientIds;

	public PacketC2SAppointments() {

	}

	public PacketC2SAppointments(List<Appointment> appointments) {
	    this.appointments = appointments;
	    this.usernames = this.appointments.stream().map(a -> a.patient.user.username).collect(Collectors.toList());
	    this.patientIds = this.appointments.stream().map(a -> a.patient.id).collect(Collectors.toList());
	}

    @Override
    public Packet write(ByteBuffer buf) throws IOException {
	    buf.writeInt(this.appointments.size(), ByteOrder.BIG_ENDIAN);

	    for(int i = 0; i < this.appointments.size(); i++) {
	    	buf.writeASCII(this.usernames.get(i), ByteOrder.BIG_ENDIAN);
		    buf.writeInt(this.patientIds.get(i), ByteOrder.BIG_ENDIAN);
		    buf.writeObject(this.appointments.get(i));
	    }

        return this;
    }

    @Override
    public Packet read(ByteBuffer buf) throws IOException {
		int size = buf.readInt(ByteOrder.BIG_ENDIAN);
		this.appointments = new ArrayList<>();
		this.usernames = new ArrayList<>();
		this.patientIds = new ArrayList<>();

	    for(int i = 0; i < size; i++) {
	    	Patient patient = new Patient(null);
	    	patient.user = new User();
	    	patient.user.username = buf.readASCII(ByteOrder.BIG_ENDIAN);
	    	patient.id = buf.readInt(ByteOrder.BIG_ENDIAN);
	    	this.usernames.add(patient.user.username);
	    	this.patientIds.add(patient.id);
		    this.appointments.add(buf.readObject(new Appointment(patient)));
	    }

        return this;
    }

	@Override
	public Packet onPacketReceived(ServerContext context) {
		try {
			for(Appointment appointment : this.appointments) {
				appointment.write(context.server.getDatabase());
			}

			context.serverListener.sendPacketToAllExcept(new PacketS2CAccount(context.server.getDatabase(), null), context.listener);
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
