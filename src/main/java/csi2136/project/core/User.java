package csi2136.project.core;

import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.buffer.IByteSerializable;

import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User implements IByteSerializable<User>, ISQLSerializable<User> {

	public String username;
	public Employee employee;
	public String passwordHash;
	public String passwordSalt;
	public String insurance;
	public List<Patient> patients = new ArrayList<>();

	public User() {

	}

	@Override
	public User write(ByteBuffer buf) throws IOException {
		buf.writeASCII(this.username, ByteOrder.BIG_ENDIAN);
		buf.writeBoolean(this.employee != null);
		if(this.employee != null) buf.writeObject(this.employee);
		buf.writeASCII(this.passwordHash, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.passwordSalt, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.insurance, ByteOrder.BIG_ENDIAN);
		buf.writeInt(this.patients.size(), ByteOrder.BIG_ENDIAN);

		for(Patient patient : this.patients) {
			buf.writeObject(patient);
		}

		return this;
	}

	@Override
	public User read(ByteBuffer buf) throws IOException {
		this.username = buf.readASCII(ByteOrder.BIG_ENDIAN);
		if(buf.readBoolean()) this.employee = buf.readObject(new Employee());
		this.passwordHash = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.passwordSalt = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.insurance = buf.readASCII(ByteOrder.BIG_ENDIAN);
		int size = buf.readInt(ByteOrder.BIG_ENDIAN);

		for(int i = 0; i < size; i++) {
			this.patients.add(buf.readObject(new Patient(this)));
		}

		return this;
	}

	@Override
	public User write(Database db) throws SQLException {
		db.send(String.format("DELETE FROM User WHERE '%s' = Username;", this.username));

		db.send(String.format("REPLACE INTO User VALUES('%s', %s, '%s', '%s', '%s')",
			this.username, this.employee == null ? "NULL" : this.employee.id, this.passwordHash, this.passwordSalt, this.insurance));

		db.send(String.format("DELETE FROM Patient WHERE '%s' = Username;", this.username));

		for(Patient patient : this.patients) {
			patient.write(db);
		}

		return this;
	}

	@Override
	public User read(ResultSet result, Database db) throws SQLException {
		this.username = result.getString("Username");

		int employeeId = result.getInt("Employee_ID");
		ResultSet er = db.send(String.format("SELECT * FROM Employee WHERE %d = Employee_ID;", employeeId));

		if(employeeId > 0 && er.next()) {
			this.employee = new Employee().read(er, db);
		}

		this.passwordHash = result.getString("Password_hash");
		this.passwordSalt = result.getString("Password_salt");
		this.insurance = result.getString("Insurance");

		result = db.send(String.format("SELECT * FROM Patient WHERE '%s' = Username;", this.username));
		List<Patient> patients = new ArrayList<>();

		while(result.next()) {
			Patient patient = new Patient(this).read(result, db);
			patients.add(patient);
		}

		this.patients = patients;

		return this;
	}

}
