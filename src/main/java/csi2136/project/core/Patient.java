package csi2136.project.core;

import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.buffer.IByteSerializable;

import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Patient implements IByteSerializable<Patient>, ISQLSerializable<Patient> {

	public User user;

	public int id;
	public String firstName;
	public String middleName;
	public String lastName;
	public int houseNumber;
	public String street;
	public String city;
	public Province province;
	public Gender gender;
	public String email;
	public String birthDate;
	public String phoneNumber;
	public String ssn;
	public List<Appointment> appointments = new ArrayList<>();

	public Patient(User parent) {
		this.user = parent;
	}

	public String getFullName() {
		return this.firstName
			+ (this.middleName == null || this.middleName.isEmpty() ? "" : " " + this.middleName)
			+ " " + this.lastName;
	}

	@Override
	public Patient write(ByteBuffer buf) throws IOException {
		buf.writeInt(this.id, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.firstName, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.middleName, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.lastName, ByteOrder.BIG_ENDIAN);
		buf.writeInt(this.houseNumber, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.street, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.city, ByteOrder.BIG_ENDIAN);
		buf.writeInt(this.province.ordinal(), ByteOrder.BIG_ENDIAN);
		buf.writeInt(this.gender.ordinal(), ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.email, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.birthDate, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.phoneNumber, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.ssn, ByteOrder.BIG_ENDIAN);
		buf.writeInt(this.appointments.size(), ByteOrder.BIG_ENDIAN);

		for(Appointment appointment : this.appointments) {
			buf.writeObject(appointment);
		}

		return this;
	}

	@Override
	public Patient read(ByteBuffer buf) throws IOException {
		this.id = buf.readInt(ByteOrder.BIG_ENDIAN);
		this.firstName = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.middleName = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.lastName = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.houseNumber = buf.readInt(ByteOrder.BIG_ENDIAN);
		this.street = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.city = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.province = Province.values()[buf.readInt(ByteOrder.BIG_ENDIAN)];
		this.gender = Gender.values()[buf.readInt(ByteOrder.BIG_ENDIAN)];
		this.email = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.birthDate = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.phoneNumber = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.ssn = buf.readASCII(ByteOrder.BIG_ENDIAN);
		int size = buf.readInt(ByteOrder.BIG_ENDIAN);

		for(int i = 0; i < size; i++) {
			this.appointments.add(buf.readObject(new Appointment(this)));
		}

		return this;
	}

	@Override
	public Patient write(Database db) throws SQLException {
		db.send(String.format("DELETE FROM Patient WHERE %d = Patient_ID;", this.id));

		db.send(String.format("REPLACE INTO Patient VALUES(%d, '%s','%s', '%s', '%s', %d, '%s', '%s', " +
			"'%s', '%s', '%s','%s', '%s', '%s')",
			this.id, this.user.username, this.firstName, this.middleName, this.lastName, this.houseNumber, this.street,
			this.city, this.province.getFormat(), this.gender.getFormat(), this.email, this.birthDate, this.phoneNumber, this.ssn));

		db.send(String.format("DELETE FROM Appointment WHERE %d = Patient_ID;", this.id));

		for(Appointment appointment : this.appointments) {
			appointment.write(db);
		}

		return this;
	}

	@Override
	public Patient read(ResultSet result, Database db) throws SQLException {
		this.id = result.getInt("Patient_ID");
		this.firstName = result.getString("First_name");
		this.middleName = result.getString("Middle_name");
		this.lastName = result.getString("Last_name");
		this.houseNumber = result.getInt("House_number");
		this.street = result.getString("Street");
		this.city = result.getString("City");
		this.province = Province.fromFormat(result.getString("Province"));
		this.gender = Gender.fromFormat(result.getString("Gender"));
		this.email = result.getString("Email_address");
		this.birthDate = result.getDate("Date_of_birth").toString();
		this.phoneNumber = result.getString("Phone_number");
		this.ssn = String.valueOf(result.getInt("SSN"));

		result = db.send(String.format("SELECT * FROM Appointment WHERE %d = Patient_ID;", this.id));
		List<Appointment> appointments = new ArrayList<>();

		while(result.next()) {
			Appointment appointment = new Appointment(this).read(result, db);
			appointments.add(appointment);
		}

		this.appointments = appointments;
		return this;
	}
	
}
