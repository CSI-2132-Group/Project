package csi2136.project.core;

import csi2136.project.AccountType;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.buffer.IByteSerializable;

import java.io.IOException;
import java.nio.ByteOrder;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee implements IByteSerializable<Employee>, ISQLSerializable<Employee> {

	public int id;
	public Branch branch;
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
	public AccountType type;
	public int salary;

	public Employee() {

	}

	public String getFullName() {
		return this.firstName
			+ (this.middleName == null || this.middleName.isEmpty() ? "" : " " + this.middleName)
			+ " " + this.lastName;
	}

	@Override
	public Employee write(ByteBuffer buf) throws IOException {
		buf.writeInt(this.id, ByteOrder.BIG_ENDIAN);
		buf.writeObject(this.branch);
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
		buf.writeInt(this.type.ordinal(), ByteOrder.BIG_ENDIAN);
		buf.writeInt(this.salary, ByteOrder.BIG_ENDIAN);
		return this;
	}

	@Override
	public Employee read(ByteBuffer buf) throws IOException {
		this.id = buf.readInt(ByteOrder.BIG_ENDIAN);
		this.branch = buf.readObject(new Branch());
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
		this.type = AccountType.values()[buf.readInt(ByteOrder.BIG_ENDIAN)];
		this.salary = buf.readInt(ByteOrder.BIG_ENDIAN);
		return this;
	}

	@Override
	public Employee write(Database db) throws SQLException {
		db.send(String.format("DELETE FROM Employee WHERE %d = Employee_ID;", this.id));

		db.send(String.format("REPLACE INTO Employee VALUES(%d, %d,'%s', '%s', '%s', %d, '%s', '%s', " +
				"'%s', '%s', '%s','%s', '%s', '%s', %d, %d)",
			this.id, this.branch.id, this.firstName, this.middleName, this.lastName, this.houseNumber, this.street,
			this.city, this.province.getFormat(), this.gender.getFormat(), this.email, this.birthDate, this.phoneNumber,
			this.ssn, this.type.ordinal(), this.salary));

		return this;
	}

	@Override
	public Employee read(ResultSet result, Database db) throws SQLException {
		this.id = result.getInt("Employee_ID");

		int branchId = result.getInt("Branch_ID");
		ResultSet br = db.send(String.format("SELECT * FROM Branch WHERE %d = Branch_ID;", branchId));

		if(br.next()) {
			this.branch = new Branch().read(br, db);
		}

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
		this.type = AccountType.values()[result.getInt("Employee_type")];
		this.salary = result.getInt("Salary");
		return this;
	}

	public static class Branch implements IByteSerializable<Branch>, ISQLSerializable<Branch> {
		public int id;
		public int houseNumber;
		public String street;
		public String city;
		public Province province;
		public String name;

		@Override
		public Branch write(ByteBuffer buf) throws IOException {
			buf.writeInt(this.id, ByteOrder.BIG_ENDIAN);
			buf.writeInt(this.houseNumber, ByteOrder.BIG_ENDIAN);
			buf.writeASCII(this.street, ByteOrder.BIG_ENDIAN);
			buf.writeASCII(this.city, ByteOrder.BIG_ENDIAN);
			buf.writeInt(this.province.ordinal(), ByteOrder.BIG_ENDIAN);
			buf.writeASCII(this.name, ByteOrder.BIG_ENDIAN);
			return this;
		}

		@Override
		public Branch read(ByteBuffer buf) throws IOException {
			this.id = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.houseNumber = buf.readInt(ByteOrder.BIG_ENDIAN);
			this.street = buf.readASCII(ByteOrder.BIG_ENDIAN);
			this.city = buf.readASCII(ByteOrder.BIG_ENDIAN);
			this.province = Province.values()[buf.readInt(ByteOrder.BIG_ENDIAN)];
			this.name = buf.readASCII(ByteOrder.BIG_ENDIAN);
			return this;
		}

		@Override
		public Branch write(Database db) throws SQLException {
			db.send(String.format("DELETE FROM Branch WHERE %d = Branch_ID;", this.id));

			db.send(String.format("REPLACE INTO Branch VALUES(%d, %d, '%s', '%s', '%s', '%s');",
				this.id, this.houseNumber, this.street, this.city, this.province.getFormat(), this.name));
			return this;
		}

		@Override
		public Branch read(ResultSet result, Database db) throws SQLException {
			this.id = result.getInt("Branch_ID");
			this.houseNumber = result.getInt("House_number");
			this.street = result.getString("Street");
			this.city = result.getString("City");
			this.province = Province.fromFormat(result.getString("Province"));
			this.name = result.getString("Name");
			return this;
		}
	}

}
