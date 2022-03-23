package csi2136.project;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DBHandler {
    private static DBHandler inst;

    private final String host;
    private final String port;
    private final String name;
    private final String userName;
    private final String password;

    private DBHandler(){
        Map<String, String> env = System.getenv();

        host = DBConnectionData.host;
        port = DBConnectionData.port;
        name = DBConnectionData.name;

        userName = DBConnectionData.userName;
        password = DBConnectionData.password;

        System.out.println("Host:"+host);
        System.out.println("port:"+port);
        System.out.println("name:"+name);
        System.out.println("userName:"+userName);
        System.out.println("password:"+password);
    }

    public static DBHandler getInst(){
        if (inst == null) {
            inst = new DBHandler();
        }

        return inst;
    }

    public static void main(String[] args) throws SQLException{
        DBHandler inst = getInst();

        dropTables();

        try {
            inst.getQuery(
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
            inst.getQuery(
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
                                "Salary MEDIUMINT, " +

                                "PRIMARY KEY(Employee_ID), " +
                                "Foreign KEY(Branch_ID) REFERENCES Branch(Branch_ID)" +
                            ")");
            inst.getQuery(
                    "CREATE TABLE User" +
                            "(" +
                            "Username VARCHAR(32) NOT NULL," +
                            "Employee_ID INT," +
                            "Password_Hash VARCHAR(32)," +
                            "Password_Salt VARCHAR(32)," +
                            "Insurance INT," +

                            "PRIMARY KEY(Username)," +
                            "Foreign KEY(Employee_ID) REFERENCES Employee(Employee_ID)" +
                            ")");
            inst.getQuery(
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

            inst.getQuery(
                    "CREATE TABLE Procedures" +
                            "(" +
                            "Procedure_ID INT AUTO_INCREMENT," +
                            "Procedure_type VARCHAR(32)," +
                            "Fee INT," +
                            "Late_Penalty INT NOT NULL,"+

                            "PRIMARY KEY(Procedure_ID)" +
                            ")");

            inst.getQuery(
                    "CREATE TABLE Treatment" +
                            "(" +
                            "Treatment_ID INT AUTO_INCREMENT," +
                            "Type VARCHAR(16), Tooth VARCHAR(16)," +
                            "Medication VARCHAR(32)," +
                            "Comments VARCHAR(256)," +
                            "Procedure_Num INT NOT NULL," +

                            "PRIMARY KEY(Treatment_ID)," +
                            "FOREIGN KEY(Procedure_Num) REFERENCES Procedures(Procedure_ID)"+
                            ")");

            inst.getQuery(
                    "CREATE TABLE Appointment" +
                            "(" +
                            "Appointment_ID INT AUTO_INCREMENT," +
                            "Patient_ID INT," +
                            "Employee_ID INT," +
                            "Treatment_ID INT," +
                            "Start_time DATE," +
                            "End_time DATE," +
                            "Status INT," +
                            "Appointment_type INT," +

                            "PRIMARY KEY(Appointment_ID)," +
                            "Foreign KEY(Patient_ID) REFERENCES Patient(Patient_ID)," +
                            "Foreign KEY(Employee_ID) REFERENCES Employee(Employee_ID)," +
                            "Foreign KEY(Treatment_ID) REFERENCES Treatment(Treatment_ID)" +
                            ")");

            inst.getQuery(
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

            inst.getQuery(
                    "CREATE TABLE Treatment_Instructions" +
                            "(" +
                            "Treatment_ID INT," +
                            "Procedure_ID INT," +
                            "PRIMARY KEY(Treatment_ID, Procedure_ID),"+
                            "Foreign KEY(Treatment_ID) REFERENCES Treatment(Treatment_ID)," +
                            "Foreign KEY(Procedure_ID) REFERENCES Procedures(Procedure_ID)" +
                            ")");

            inst.getQuery(
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

            inst.getQuery(
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
            inst.getQuery(
                    "INSERT INTO branch VALUES" +
                            "(DEFAULT,6437, 'Orleans', 'Ottawa', 'ON', 'Main Branch')," +
                            "(DEFAULT,1234, 'First','Toronto', 'ON', 'Second Branch')," +
                            "(DEFAULT, 7864, 'Bronson', 'Montreal', 'QB', 'Third Branch')"
            );

            //Employee
            inst.getQuery(
                    "INSERT INTO employee VALUES" +
                            "(DEFAULT, 1, 'John', 'Maxwell', 'Doe', 3434, 'Main', 'Ottawa', 'ON', 'M', 'johndoe@email.com', '2000-10-03', '6138639876', 456781234, 1, 90000)," +
                            "(DEFAULT, 1, 'James', 'Tod', 'Jackson', 5456, 'Elgin', 'Ottawa', 'ON', 'M', 'jamesjack@email.com', '1999-11-04', '6134327869', 332211556, 2, 80000)," +
                            "(DEFAULT, 1, 'Mary', 'Elizabeth', 'Taylor', 7653, 'Hunt Club', 'Ottawa', 'ON', 'F', 'marytaylor@email.com', '1998-08-08', '6133479812', 444555666, 3, 70000)," +
                            "(DEFAULT, 2, 'Michael', 'Douglas', 'Owen', 2121, 'First', 'Toronto', 'ON', 'M', 'michaelowen@email.com', '2000-11-10', '6137777777', 123456789, 1, 90000)," +
                            "(DEFAULT, 2, 'Emily', 'Lea', 'Fitzgerald', 5656, 'Second', 'Toronto', 'ON', 'F', 'emilyfitz@email.com', '1999-09-01', '6138888888', 987654321, 2, 80000)," +
                            "(DEFAULT, 2, 'Thomas', 'Tyler', 'Jones', 9898, 'Third', 'Toronto', 'ON', 'M', 'tomjones@email.com', '1998-02-02', '6139999999', 876543129, 3, 70000)," +
                            "(DEFAULT, 3, 'Jim', 'Timothy', 'Dalton', 7777, 'Fourth', 'Montreal', 'QB', 'M', 'jimdalton@email.com', '2000-03-05', '6131111111', 333888111, 1, 90000)," +
                            "(DEFAULT, 3, 'Louise', 'Sofie', 'Muir', 6767, 'Fifth', 'Montreal', 'QB', 'F', 'louisemuir@email.com', '1999-07-02', '6132222222', 999333777, 2, 80000)," +
                            "(DEFAULT, 3, 'Joseph', 'David', 'Johnson', 8743, 'Sixth', 'Montreal', 'QB', 'M', 'davidjohnson@email.com', '1998-04-09', '6133333333', 777333111, 3, 70000)"
            );

            //Users
            inst.getQuery(
                    //TODO create passwords
                    "INSERT INTO user VALUES" +
                            "('johndoe', 1, NULL, NULL, '123456')," + //Make this password EmployeeTest
                            "('jamesjackson', 2, NULL, NULL, '654321')," +
                            "('marytaylor', 3, NULL, NULL, '987654')," +
                            "('michaelowen', 4, NULL, NULL, '876543')," +
                            "('emilyfitzgerald', 5, NULL, NULL, '765432')," +
                            "('thomasjones', 6, NULL, NULL, '234567')," +
                            "('jimdalton', 7, NULL, NULL, '345678')," +
                            "('louisemuir', 8, NULL, NULL, '456789')," +
                            "('josephjohnson', 9, NULL, NULL, '567890')," +
                            "('WilmaWills', NULL, NULL, NULL, '123456')," +
                            "('ShaunFrancis', NULL, NULL, NULL, '702456')"
            );


            //TODO Patients
            inst.getQuery(
                    "INSERT INTO patient VALUES" +
                            "(DEFAULT, 'ShaunFrancis', 'Shaun', 'Akash', 'Francis', '2002','Oakbrook Circle','Ottawa', 'On', 'M', 'shaun.akash@gmail.com', '2002-01-13', '6138306708', '123456789')"
            );

            inst.getQuery(
                    "INSERT INTO patient VALUES" +
                            "(DEFAULT, 'WilmaWills', 'Wilma', 'Wonky', 'Wills', '1949','Strom Olsen','Ottawa', 'On', 'F', 'wilma.wills@gmail.com', '2000-10-02', '6138456808', '702456675')"
            );

            //TODO Procedures
            inst.getQuery(
                    "INSERT INTO Procedures VALUES"+
                            "(DEFAULT, 'Cleaning', 20, 5)"
            );
            inst.getQuery(
                    "INSERT INTO Procedures VALUES"+
                            "(DEFAULT, 'Cavity', 45, 12)"
            );
            inst.getQuery(
                    "INSERT INTO Procedures VALUES"+
                            "(DEFAULT, 'Check Up', 10, 3)"
            );
            inst.getQuery(
                    "INSERT INTO Procedures VALUES"+
                            "(DEFAULT, 'Wisdom Teeth', 75, 19)"
            );
            inst.getQuery(
                    "INSERT INTO Procedures VALUES"+
                            "(DEFAULT, 'Crowns', 87, 22)"
            );

            //TODO Treatments
            inst.getQuery(
                    "INSERT INTO treatment VALUES" +
                            "(DEFAULT, 'Filling', 'Canine', 'Morphine', 'Right Canine has cavity and requires filling', 2)"
            );
            inst.getQuery(
                    "INSERT INTO treatment VALUES" +
                            "(DEFAULT, 'Cleaning', 'Mouth', 'N/A', 'Standard Teeth Cleaning Appointment', 1)"
            );
            inst.getQuery(
                    "INSERT INTO treatment VALUES" +
                            "(DEFAULT, 'Cleaning', 'Mouth', 'N/A', 'Standard Teeth Cleaning Appointment', 1)"
            );

            //TODO Treatment_Instructions
            inst.getQuery(
                    "INSERT INTO Treatment_Instructions VALUES"+
                            "(1, 2)"
            );
            inst.getQuery(
                    "INSERT INTO Treatment_Instructions VALUES"+
                            "(2, 1)"
            );
            inst.getQuery(
                    "INSERT INTO Treatment_Instructions VALUES"+
                            "(3, 1)"
            );

            //TODO Appointment
            inst.getQuery(
                  "INSERT INTO Appointment VALUES"+
                        "(DEFAULT, 1, 1, 1, '2022-03-17', '2022-03-17', 1, 3)"
            );
            inst.getQuery(
                    "INSERT INTO Appointment VALUES"+
                            "(DEFAULT, 1, 2, 2, '2022-03-17', '2022-03-17', 1, 3)"
            );
            inst.getQuery(
                    "INSERT INTO Appointment VALUES"+
                            "(DEFAULT, 1, 2, 3, '2022-03-17', '2022-03-17', 1, 3)"
            );

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    static private void dropTables() throws SQLException {
        DBHandler inst = getInst();

        ResultSet result_set = inst.getQuery("SHOW TABLES");

        Queue<String> table_names = new LinkedList<>();

        while(result_set.next()) {
            table_names.add(result_set.getString(1));
        }
        //result_set.close();
        while(!table_names.isEmpty())
        {
            String table_name = table_names.poll();
            try
            {
                inst
                        .getConnection()
                        .createStatement()
                        .execute("SET FOREIGN_KEY_CHECKS=0");
                inst
                        .getConnection()
                        .createStatement()
                        .execute("DROP TABLE "+table_name);
            }
            catch (Exception e)
            {
                table_names.add(table_name);
                System.out.println(e);
            }

            //inst.getQuery("DROP TABLE "+table_names.pop());
        }

        inst.getQuery("SET FOREIGN_KEY_CHECKS=1");
    }

    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
            "jdbc:mysql://"+host+":"+port+"/"+name,
                userName,
                password
        );
    }

    public ResultSet getQuery(String query) throws SQLException {
        Statement stmp = this.getConnection().createStatement();

        stmp.execute(query);

        return stmp.getResultSet();
    }
}
