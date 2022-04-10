package csi2136.project.core;

import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.buffer.IByteSerializable;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class Appointment implements IByteSerializable<Appointment>, ISQLSerializable<Appointment> {

	public Patient patient;

	public int id = -1;
	public Employee employee;
	public Treatment treatment;
	public String date;
	public String startTime;
	public String endTime;
	public Status status;
	public String type;
	public Invoice invoice;
	public String review;

	public Appointment(Patient patient) {
		this.patient = patient;
	}

	public String getFullTime() {
		String[] a = this.startTime.split(Pattern.quote(":"));
		String[] b = this.endTime.split(Pattern.quote(":"));
		return String.format("%s (%s:%s to %s:%s)", this.date, a[0], a[1], b[0], b[1]);
	}

	@Override
	public Appointment write(ByteBuffer buf) throws IOException {
		buf.writeInt(this.id, ByteOrder.BIG_ENDIAN);
		buf.writeObject(this.employee);
		buf.writeBoolean(this.treatment != null);
		if(this.treatment != null) buf.writeObject(this.treatment);
		buf.writeASCII(this.date, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.startTime, ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.endTime, ByteOrder.BIG_ENDIAN);
		buf.writeInt(this.status.ordinal(), ByteOrder.BIG_ENDIAN);
		buf.writeASCII(this.type, ByteOrder.BIG_ENDIAN);
		buf.writeBoolean(this.invoice != null);
		if(this.invoice != null) buf.writeObject(this.invoice);
		buf.writeBoolean(this.review != null);
		if(this.review != null) buf.writeASCII(this.review, ByteOrder.BIG_ENDIAN);
		return this;
	}

	@Override
	public Appointment read(ByteBuffer buf) throws IOException {
		this.id = buf.readInt(ByteOrder.BIG_ENDIAN);
		this.employee = buf.readObject(new Employee());
		if(buf.readBoolean()) this.treatment = buf.readObject(new Treatment());
		this.date = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.startTime = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.endTime = buf.readASCII(ByteOrder.BIG_ENDIAN);
		this.status = Status.values()[buf.readInt(ByteOrder.BIG_ENDIAN)];
		this.type = buf.readASCII(ByteOrder.BIG_ENDIAN);
		if(buf.readBoolean()) this.invoice = buf.readObject(new Invoice(this));
		if(buf.readBoolean()) this.review = buf.readASCII(ByteOrder.BIG_ENDIAN);
		return this;
	}

	@Override
	public Appointment write(Database db) throws SQLException {
		if(this.id != -1) {
			db.send(String.format("DELETE FROM Appointment WHERE %d = Appointment_ID;", this.id));
		}

		if(this.treatment != null) {
			this.treatment.write(db);
		}

		if(this.invoice != null) {
			this.invoice.write(db);
		}

		db.send(String.format("REPLACE INTO Appointment VALUES(%s, %d, %d, %s, '%s', '%s', '%s', %d, '%s', %s);",
			this.id == -1 ? "DEFAULT" : this.id, this.patient.id, this.employee.id,
			this.treatment == null ? "NULL" : this.treatment.id, this.date,
			this.startTime, this.endTime, this.status.ordinal(), this.type, this.review == null ? "NULL" : "'" + this.review + "'"));
		return this;
	}

	@Override
	public Appointment read(ResultSet result, Database db) throws SQLException {
		this.id = result.getInt("Appointment_ID");

		int employeeId = result.getInt("Employee_ID");
		ResultSet er = db.send(String.format("SELECT * FROM Employee WHERE %d = Employee_ID;", employeeId));

		if(er.next()) {
			this.employee = new Employee().read(er, db);
		}

		int treatmentId = result.getInt("Treatment_ID");
		ResultSet tr = db.send(String.format("SELECT * FROM Treatment WHERE %d = Treatment_ID;", treatmentId));

		if(tr.next()) {
			this.treatment = new Treatment().read(tr, db);
		}

		this.date = result.getDate("Date").toString();
		this.startTime = result.getTime("Start_time").toString();
		this.endTime = result.getTime("End_time").toString();
		this.status = Status.values()[result.getInt("Status")];

		this.type = result.getString("Appointment_type");

		ResultSet ir = db.send(String.format("SELECT * FROM Invoice WHERE %d = Appointment;", this.id));

		if(ir.next()) {
			this.invoice = new Invoice(this).read(ir, db);
		}

		this.review = result.getString("Review");
		return this;
	}

	public enum Status {
		SCHEDULED(new Color(0.39215687f, 0.58431375f, 0.92941177f)),
		COMPLETED(new Color(0.0f, 0.5019608f, 0.0f)),
		CANCELLED(new Color(1.0f, 0.0f, 0.0f));

		private final Color color;

		Status(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return this.color;
		}
	}

	public static class Treatment implements IByteSerializable<Treatment>, ISQLSerializable<Treatment> {
		public int id = -1;
		public String type;
		public String tooth;
		public String medication;
		public String comments;
		public Procedure procedure = new Procedure();

		@Override
		public Treatment write(ByteBuffer buf) throws IOException {
			buf.writeInt(this.id, ByteOrder.BIG_ENDIAN);
			buf.writeASCII(this.type, ByteOrder.BIG_ENDIAN);
			buf.writeASCII(this.tooth, ByteOrder.BIG_ENDIAN);
			buf.writeASCII(this.medication, ByteOrder.BIG_ENDIAN);
			buf.writeASCII(this.comments, ByteOrder.BIG_ENDIAN);
			buf.writeObject(this.procedure);
			return this;
		}

		@Override
		public Treatment read(ByteBuffer buf) throws IOException {
			this.id = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.type = buf.readASCII(ByteOrder.BIG_ENDIAN);
			this.tooth = buf.readASCII(ByteOrder.BIG_ENDIAN);
			this.medication = buf.readASCII(ByteOrder.BIG_ENDIAN);
			this.comments = buf.readASCII(ByteOrder.BIG_ENDIAN);
			this.procedure = buf.readObject(new Procedure());
			return this;
		}

		@Override
		public Treatment write(Database db) throws SQLException {
			db.send(String.format("DELETE FROM Treatment WHERE %d = Treatment_ID;", this.id));

			db.send(String.format("REPLACE INTO Treatment VALUES(%s, '%s', '%s', '%s', '%s', %d);",
				this.id == -1 ? "DEFAULT" : this.id, this.type, this.tooth, this.medication, this.comments, this.procedure.id));
			return this;
		}

		@Override
		public Treatment read(ResultSet result, Database db) throws SQLException {
			this.id = result.getInt("Treatment_ID");
			this.type = result.getString("Type");
			this.tooth = result.getString("Tooth");
			this.medication = result.getString("Medication");
			this.comments = result.getString("Comments");

			int procedureId = result.getInt("Procedure_Num");
			ResultSet ps = db.send(String.format("SELECT * FROM Procedures WHERE %s = Procedure_ID;", procedureId));

			if(ps.next()) {
				this.procedure = new Procedure().read(ps, db);
			}

			return this;
		}
	}

	public static class Procedure implements IByteSerializable<Procedure>, ISQLSerializable<Procedure> {
		public int id;
		public String type;
		public int fee;
		public int latePenalty;

		@Override
		public Procedure write(ByteBuffer buf) throws IOException {
			buf.writeInt(this.id, ByteOrder.BIG_ENDIAN);
			buf.writeASCII(this.type, ByteOrder.BIG_ENDIAN);
			buf.writeInt(this.fee, ByteOrder.BIG_ENDIAN);
			buf.writeInt(this.latePenalty, ByteOrder.BIG_ENDIAN);
			return this;
		}

		@Override
		public Procedure read(ByteBuffer buf) throws IOException {
			this.id = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.type = buf.readASCII(ByteOrder.BIG_ENDIAN);
			this.fee = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.latePenalty = buf.readInt(ByteOrder.BIG_ENDIAN);
			return this;
		}

		@Override
		public Procedure write(Database db) throws SQLException {
			db.send(String.format("DELETE FROM Procedures WHERE %d = Procedure_ID;", this.id));

			db.send(String.format("REPLACE INTO Procedures VALUES(%d, '%s', %d, %d);",
				this.id, this.type, this.fee, this.latePenalty));
			return this;
		}

		@Override
		public Procedure read(ResultSet result, Database db) throws SQLException {
			this.id = result.getInt("Procedure_ID");
			this.type = result.getString("Procedure_type");
			this.fee = result.getInt("Fee");
			this.latePenalty = result.getInt("Late_Penalty");
			return this;
		}
	}

	public static class Invoice implements IByteSerializable<Invoice>, ISQLSerializable<Invoice> {
		private final Appointment appointment;

		public String date = "2022-05-01";
		public int insuranceCharge = 500;
		public int patientCharge = 20;
		public int discount = 0;
		public int penalty = 15;

		public Invoice(Appointment parent) {
			this.appointment = parent;
		}

		@Override
		public Invoice write(ByteBuffer buf) throws IOException {
			buf.writeASCII(this.date, ByteOrder.BIG_ENDIAN);
			buf.writeInt(this.insuranceCharge, ByteOrder.BIG_ENDIAN);
			buf.writeInt(this.patientCharge, ByteOrder.BIG_ENDIAN);
			buf.writeInt(this.discount, ByteOrder.BIG_ENDIAN);
			buf.writeInt(this.penalty, ByteOrder.BIG_ENDIAN);
			return this;
		}

		@Override
		public Invoice read(ByteBuffer buf) throws IOException {
			this.date = buf.readASCII(ByteOrder.BIG_ENDIAN);
			this.insuranceCharge = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.patientCharge = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.discount = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.penalty = buf.readInt(ByteOrder.BIG_ENDIAN);
			return this;
		}

		@Override
		public Invoice write(Database db) throws SQLException {
			db.send(String.format("DELETE FROM Invoice WHERE ('%s' = Payee) AND (%d = Appointment);",
				this.appointment.patient.user.username, this.appointment.id));

			db.send(String.format("REPLACE INTO Invoice VALUES('%s', %d, '%s', %d, %d, %d, %d);",
				this.appointment.patient.user.username, this.appointment.id,
				this.date, this.insuranceCharge, this.patientCharge, this.discount, this.penalty));

			return this;
		}

		@Override
		public Invoice read(ResultSet result, Database db) throws SQLException {
			this.date = result.getDate("Date_of_issue").toString();
			this.insuranceCharge = result.getInt("Insurance_charge");
			this.patientCharge = result.getInt("Patient_charge");
			this.discount = result.getInt("Discount");
			this.penalty = result.getInt("Penalty");
			return this;
		}
	}

}
