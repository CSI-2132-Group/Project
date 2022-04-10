package csi2136.project.core;

import csi2136.project.Server;

import java.sql.*;
import java.util.function.Consumer;

public class Database {

	private Server server;
	private Connection connection;

	public Database(Server server) {
		this.server = server;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public boolean isConnected() {
		try {
			return this.connection != null && !this.connection.isClosed();
		} catch(SQLException e) {
			return false;
		}
	}

	public boolean connect(String url, String user, String password, Consumer<SQLException> onError) {
		try {
			this.connection = DriverManager.getConnection(url, user, password);
			return true;
		} catch(SQLException e) {
			onError.accept(e);
			return false;
		}
	}

	public ResultSet send(String query) {
		return this.send(e -> {
			this.server.disconnect(e.getMessage());
			e.printStackTrace();
		}, query);
	}

	public ResultSet send(Consumer<SQLException> onError, String query) {
		try {
			Statement statement = this.getConnection().createStatement();
			statement.execute(query);
			return statement.getResultSet();
		} catch(SQLException e) {
			onError.accept(e);
		}

		return null;
	}

	public ResultSet[] send(String... queries) {
		return this.send(e -> {
			this.server.disconnect(e.getMessage());
			e.printStackTrace();
		}, queries);
	}

	public ResultSet[] send(Consumer<SQLException> onError, String... queries) {
		ResultSet[] result = new ResultSet[queries.length];

		try {
			for(int i = 0; i < queries.length; i++) {
				Statement statement = this.getConnection().createStatement();
				statement.execute(queries[i]);
				result[i] = statement.getResultSet();
			}
		} catch(SQLException e) {
			onError.accept(e);
		}

		return result;
	}

	public static void initialize(Database db) {
		try {
			db.send(
				"CREATE TABLE Branch" +
					"(" +
					"Branch_ID INT AUTO_INCREMENT," +
					"House_number MEDIUMINT," +
					"Street VARCHAR(32)," +
					"City VARCHAR(32)," +
					"Province CHAR(2)," +
					"Name VARCHAR(64)," +
					"PRIMARY KEY(Branch_ID)" +
					")");
			db.send(
				"CREATE TABLE Employee" +
					"(" +
					"Employee_ID INT AUTO_INCREMENT," +
					"Branch_ID INT," +
					"First_name VARCHAR(32)," +
					"Middle_name VARCHAR(32)," +
					"Last_name VARCHAR(32)," +
					"House_number MEDIUMINT," +
					"Street VARCHAR(32)," +
					"City VARCHAR(32)," +
					"Province CHAR(2)," +
					"Gender Char(1)," +
					"Email_address VARCHAR(32)," +
					"Date_of_birth DATE," +
					"Phone_number VARCHAR(16)," +
					"SSN INT NOT NULL CHECK (ssn between 0 and 999999999)," +
					"Employee_type INT," +
					"Salary INT, " +

					"PRIMARY KEY(Employee_ID), " +
					"Foreign KEY(Branch_ID) REFERENCES Branch(Branch_ID)" +
					")");
			db.send(
				"CREATE TABLE User" +
					"(" +
					"Username VARCHAR(32) NOT NULL," +
					"Employee_ID INT," +
					"Password_Hash VARCHAR(64)," +
					"Password_Salt VARCHAR(16)," +
					"Insurance VARCHAR(16)," +

					"PRIMARY KEY(Username)," +
					"Foreign KEY(Employee_ID) REFERENCES Employee(Employee_ID)" +
					")");
			db.send(
				"CREATE TABLE Patient" +
					"(" +
					"Patient_ID INT AUTO_INCREMENT," +
					"Username VARCHAR(32)," +
					"First_name VARCHAR(32)," +
					"Middle_name VARCHAR(32)," +
					"Last_name VARCHAR(32)," +
					"House_number MEDIUMINT," +
					"Street VARCHAR(32)," +
					"City VARCHAR(32)," +
					"Province CHAR(2)," +
					"Gender CHAR(1)," +
					"Email_address VARCHAR(32)," +
					"Date_of_birth DATE," +
					"Phone_number VARCHAR(16)," +
					"SSN INT NOT NULL CHECK (ssn between 0 and 999999999)," +

					"PRIMARY KEY(Patient_ID)," +
					"Foreign KEY(Username) REFERENCES User(Username)" +
					")");

			db.send(
				"CREATE TABLE Procedures" +
					"(" +
					"Procedure_ID INT AUTO_INCREMENT," +
					"Procedure_type VARCHAR(32)," +
					"Fee INT," +
					"Late_Penalty INT NOT NULL,"+

					"PRIMARY KEY(Procedure_ID)" +
					")");

			db.send(
				"CREATE TABLE Treatment" +
					"(" +
					"Treatment_ID INT AUTO_INCREMENT," +
					"Type VARCHAR(16), " +
					"Tooth VARCHAR(16)," +
					"Medication VARCHAR(32)," +
					"Comments VARCHAR(256)," +
					"Procedure_Num INT NOT NULL," +

					"PRIMARY KEY(Treatment_ID)," +
					"FOREIGN KEY(Procedure_Num) REFERENCES Procedures(Procedure_ID)"+
					")");

			db.send(
				"CREATE TABLE Appointment" +
					"(" +
					"Appointment_ID INT AUTO_INCREMENT," +
					"Patient_ID INT," +
					"Employee_ID INT," +
					"Treatment_ID INT," +
					"Date DATE," +
					"Start_time TIME," +
					"End_time TIME," +
					"Status INT," +
					"Appointment_type VARCHAR(16)," +
					"Review VARCHAR(256)," +

					"PRIMARY KEY(Appointment_ID)," +
					"Foreign KEY(Patient_ID) REFERENCES Patient(Patient_ID)," +
					"Foreign KEY(Employee_ID) REFERENCES Employee(Employee_ID)," +
					"Foreign KEY(Treatment_ID) REFERENCES Treatment(Treatment_ID)" +
					")");

			db.send(
				"CREATE TABLE Invoice" +
					"(" +
					"Payee VARCHAR(32)," +
					"Appointment INT," +
					"Date_of_issue DATE," +
					"Insurance_charge INT," +
					"Patient_charge INT," +
					"Discount INT," +
					"Penalty INT," +

					"PRIMARY KEY(Payee,Appointment),"+
					"FOREIGN KEY(Payee) REFERENCES User (Username),"+
					"FOREIGN KEY(Appointment) REFERENCES Appointment(Appointment_ID)"+
					")");

			db.send(
				"CREATE TABLE Treatment_dbructions" +
					"(" +
					"Treatment_ID INT," +
					"Procedure_ID INT," +
					"PRIMARY KEY(Treatment_ID, Procedure_ID),"+
					"Foreign KEY(Treatment_ID) REFERENCES Treatment(Treatment_ID)," +
					"Foreign KEY(Procedure_ID) REFERENCES Procedures(Procedure_ID)" +
					")");

			db.send(
				"CREATE TABLE Review" +
					"(" +
					"Review_ID INT AUTO_INCREMENT," +
					"Appointment_ID INT," +
					"Professionalism INT," +
					"Communication INT," +
					"Cleanliness INT," +
					"Score INT," +

					"PRIMARY KEY(Review_ID)," +
					"Foreign KEY(Appointment_ID) REFERENCES Appointment(Appointment_ID)" +
					")");

			db.send(
				"CREATE TABLE Branch_location" +
					"(" +
					"Appointment_ID INT AUTO_INCREMENT," +
					"Branch_ID INT," +
					"Assigned_room INT," +

					"PRIMARY KEY(Appointment_ID,Branch_ID,Assigned_room),"+
					"Foreign KEY(Appointment_ID) REFERENCES Appointment(Appointment_ID)," +
					"Foreign KEY(Branch_ID) REFERENCES Branch(Branch_ID)" +
					")");

			//Branch
			db.send(
				"INSERT INTO branch VALUES" +
					"(DEFAULT,6437, 'Orleans', 'Ottawa', 'ON', 'Main Branch')," +
					"(DEFAULT,1234, 'First','Toronto', 'ON', 'Second Branch')," +
					"(DEFAULT, 7864, 'Bronson', 'Montreal', 'QC', 'Third Branch')"
			);

			//Employee
			db.send(
				"INSERT INTO employee VALUES" +
					"(DEFAULT, 1, 'John', 'Maxwell', 'Doe', 3434, 'Main', 'Ottawa', 'ON', 'M', 'johndoe@email.com', '2000-10-03', '6138639876', 456781234, 1, 90000)," +
					"(DEFAULT, 1, 'James', 'Tod', 'Jackson', 5456, 'Elgin', 'Ottawa', 'ON', 'M', 'jamesjack@email.com', '1999-11-04', '6134327869', 332211556, 2, 80000)," +
					"(DEFAULT, 1, 'Mary', 'Elizabeth', 'Taylor', 7653, 'Hunt Club', 'Ottawa', 'ON', 'F', 'marytaylor@email.com', '1998-08-08', '6133479812', 444555666, 3, 70000)," +
					"(DEFAULT, 2, 'Michael', 'Douglas', 'Owen', 2121, 'First', 'Toronto', 'ON', 'M', 'michaelowen@email.com', '2000-11-10', '6137777777', 123456789, 1, 90000)," +
					"(DEFAULT, 2, 'Emily', 'Lea', 'Fitzgerald', 5656, 'Second', 'Toronto', 'ON', 'F', 'emilyfitz@email.com', '1999-09-01', '6138888888', 987654321, 2, 80000)," +
					"(DEFAULT, 2, 'Thomas', 'Tyler', 'Jones', 9898, 'Third', 'Toronto', 'ON', 'M', 'tomjones@email.com', '1998-02-02', '6139999999', 876543129, 3, 70000)," +
					"(DEFAULT, 3, 'Jim', 'Timothy', 'Dalton', 7777, 'Fourth', 'Montreal', 'QC', 'M', 'jimdalton@email.com', '2000-03-05', '6131111111', 333888111, 1, 90000)," +
					"(DEFAULT, 3, 'Louise', 'Sofie', 'Muir', 6767, 'Fifth', 'Montreal', 'QC', 'F', 'louisemuir@email.com', '1999-07-02', '6132222222', 999333777, 2, 80000)," +
					"(DEFAULT, 3, 'Joseph', 'David', 'Johnson', 8743, 'Sixth', 'Montreal', 'QC', 'M', 'davidjohnson@email.com', '1998-04-09', '6133333333', 777333111, 3, 70000)"
			);

			//Users
			db.send(
				//TODO create passwords
				"INSERT INTO user VALUES" +
					"('johndoe', 1, 'sW8F41FyeiOLDvdGWMHhwKbF7BdusDAOYXAM9mz8H4M=', 'O4H04MPU8XH5XFTC', '123456')," + //Make this password EmployeeTest
					"('jamesjackson', 2, '6zfQvM7YI3bBrHt18X7oQpcLms4qtyZ524oEh2i7uLs=', 'IA7CIVDQL11IKBAM', '654321')," +
					"('marytaylor', 3, 'gDonM8iwjTwPSXsPiOX2xbWuo2irUMbUMPyev2m7G6A=', 'RE3G3E6T527CQRYS', '987654')," +
					"('michaelowen', 4, 'ZNo75E9qg5PiWw+dGq6J9beyWVndwOCS/kRzy/OhwqU=', 'X8Z9197U9R17HMXN', '876543')," +
					"('emilyfitzgerald', 5, 'gt6lHdHTZDoVljXitqQTg/mOpZoVzZajOi9rbFXkiRo=', 'GOLUNUW9B0B54WCR', '765432')," +
					"('thomasjones', 6, 'FCdZIo7iqP63qrmVfBqrg3QD1E2xyLYFlQ1G4UbmUuk=', '9QLOZBNHOBGAOREI', '234567')," +
					"('jimdalton', 7, 'uU4lPxI01nLZKv4CvbApmxU5Daoq/RLtKdQ8ajMuDU0=', 'VQ5YV8Z3L9U89411', '345678')," +
					"('louisemuir', 8, 'v6OMF2mexiMPkIWvJgXWQ8XydvLrSstQr+O39PSCtP4=', 'WUXG1OE0OO9RUKTA', '456789')," +
					"('josephjohnson', 9, '3UAOMNLLIVDNEdb1hrDZp08epljjPECUHzBdK5QwXEU=', 'ONUSQ9O0QMZVE17L', '567890')," +
					"('WilmaWills', NULL, 'YeD0gQ3mtKsyyAPgLUkPU7iu8uc9rljv6RCgdODPjMk=', 'K2KIDGMMVV69CWWQ', '123456')," +
					"('ShaunFrancis', NULL, 'e/4y+LlkrBOzFcnS91EeH9+T5rII8Rm+JXRYBGjWCO0=', '47G6AHX99U8N0KXQ', '702456')"
			);


			//TODO Patients
			db.send(
				"INSERT INTO patient VALUES" +
					"(DEFAULT, 'ShaunFrancis', 'Shaun', 'Akash', 'Francis', '2002','Oakbrook Circle','Ottawa', 'On', 'M', 'shaun.akash@gmail.com', '2002-01-13', '6138306708', '123456789')"
			);

			db.send(
				"INSERT INTO patient VALUES" +
					"(DEFAULT, 'WilmaWills', 'Wilma', 'Wonky', 'Wills', '1949','Strom Olsen','Ottawa', 'On', 'F', 'wilma.wills@gmail.com', '2000-10-02', '6138456808', '702456675')"
			);

			//TODO Procedures
			db.send(
				"INSERT INTO Procedures VALUES"+
					"(DEFAULT, 'Cleaning', 20, 5)"
			);
			db.send(
				"INSERT INTO Procedures VALUES"+
					"(DEFAULT, 'Cavity', 45, 12)"
			);
			db.send(
				"INSERT INTO Procedures VALUES"+
					"(DEFAULT, 'Check Up', 10, 3)"
			);
			db.send(
				"INSERT INTO Procedures VALUES"+
					"(DEFAULT, 'Wisdom Teeth', 75, 19)"
			);
			db.send(
				"INSERT INTO Procedures VALUES"+
					"(DEFAULT, 'Crowns', 87, 22)"
			);

			//TODO Treatments
			db.send(
				"INSERT INTO treatment VALUES" +
					"(DEFAULT, 'Filling', 'Canine', 'Morphine', 'Right Canine has cavity and requires filling', 2)"
			);
			db.send(
				"INSERT INTO treatment VALUES" +
					"(DEFAULT, 'Cleaning', 'Mouth', 'N/A', 'Standard Teeth Cleaning Appointment', 1)"
			);
			db.send(
				"INSERT INTO treatment VALUES" +
					"(DEFAULT, 'Cleaning', 'Mouth', 'N/A', 'Standard Teeth Cleaning Appointment', 1)"
			);

			//TODO Treatment_dbructions
			db.send(
				"INSERT INTO Treatment_dbructions VALUES"+
					"(1, 2)"
			);
			db.send(
				"INSERT INTO Treatment_dbructions VALUES"+
					"(2, 1)"
			);
			db.send(
				"INSERT INTO Treatment_dbructions VALUES"+
					"(3, 1)"
			);

			//TODO Appointment
			db.send(
				"INSERT INTO Appointment VALUES"+
					"(DEFAULT, 1, 1, 1, '2022-05-01', '13:30:00', '14:30:00', 1, 'Check Up', 'Outstanding Moves!')"
			);
			db.send(
				"INSERT INTO Appointment VALUES"+
					"(DEFAULT, 1, 2, 2, '2022-05-02', '13:30:00', '14:30:00', 1, 'Check Up', 'Outstanding Moves!!')"
			);
			db.send(
				"INSERT INTO Appointment VALUES"+
					"(DEFAULT, 1, 2, 3, '2022-05-03', '13:30:00', '14:30:00', 1, 'Check Up', 'Outstanding Moves!!!')"
			);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
