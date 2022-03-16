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
                            "Branch_ID INT," +
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
                                "Employee_ID INT," +
                                "Branch_ID INT," +
                                "First_name VARCHAR(32)," +
                                "Middle_name VARCHAR(32)," +
                                "Last_name VARCHAR(32)," +
                                "House_number MEDIUMINT," +
                                "Street VARCHAR(32)," +
                                "City VARCHAR(32)," +
                                "Province CHAR(2)," +
                                "Gender Binary(1)," +
                                "Email_address VARCHAR(32)," +
                                "Date_of_birth DATE," +
                                "Phone_number VARCHAR(16)," +
                                "SSN INT," +
                                "Employee_type INT," +
                                "Salary MEDIUMINT, " +

                                "PRIMARY KEY(Employee_ID), " +
                                "Foreign KEY(Branch_ID) REFERENCES Branch(Branch_ID)" +
                            ")");
            inst.getQuery(
                    "CREATE TABLE User" +
                            "(" +
                            "Username VARCHAR(32)," +
                            "Employee_ID INT," +
                            "Password BINARY(32)," +
                            "Insurance INT," +

                            "PRIMARY KEY(Username)," +
                            "Foreign KEY(Employee_ID) REFERENCES Employee(Employee_ID)" +
                            ")");
            inst.getQuery(
                    "CREATE TABLE Patient" +
                    "(" +
                        "Patient_ID INT," +
                        "Username VARCHAR(32)," +
                        "First_name VARCHAR(32)," +
                        "Middle_name VARCHAR(32)," +
                        "Last_name VARCHAR(32)," +
                        "House_number MEDIUMINT," +
                        "Street VARCHAR(32)," +
                        "City VARCHAR(32)," +
                        "Province CHAR(2)," +
                        "Gender Binary(1)," +
                        "Email_address VARCHAR(32)," +
                        "Date_of_birth DATE," +
                        "Phone_number VARCHAR(16)," +
                        "SSN INT," +

                        "PRIMARY KEY(Patient_ID)," +
                        "Foreign KEY(Username) REFERENCES User(Username)" +
                    ")");
            inst.getQuery(
                    "CREATE TABLE Invoice" +
                            "(" +
                            "Payee INT," +
                            "Appointment INT," +
                            "Date_of_issue DATE," +
                            "Insurance_charge INT," +
                            "Patient_charge INT," +
                            "Discount INT," +
                            "Penalty INT" +
                            ")");
            inst.getQuery(
                    "CREATE TABLE Treatment" +
                            "(" +
                            "Treatment_ID INT," +
                            "Type INT, Tooth INT," +
                            "Medication VARCHAR(32)," +
                            "Comments VARCHAR(256)," +
                            "Penalty INT," +

                            "PRIMARY KEY(Treatment_ID)" +
                            ")");
            inst.getQuery(
                    "CREATE TABLE Procedures" +
                            "(" +
                            "Procedure_ID INT," +
                            "Procedure_type INT," +
                            "Fee INT," +

                            "PRIMARY KEY(Procedure_ID)" +
                            ")");
            inst.getQuery(
                    "CREATE TABLE Treatment_Instructions" +
                            "(" +
                            "Treatment_ID INT," +
                            "Procedure_ID INT," +

                            "Foreign KEY(Treatment_ID) REFERENCES Treatment(Treatment_ID)," +
                            "Foreign KEY(Procedure_ID) REFERENCES Procedures(Procedure_ID)" +
                            ")");
            inst.getQuery(
                    "CREATE TABLE Appointment" +
                            "(" +
                            "Appointment_ID INT," +
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
                    "CREATE TABLE Review" +
                            "(" +
                            "Review_ID INT," +
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
                            "Appointment_ID INT," +
                            "Branch_ID INT," +
                            "Assigned_room INT," +

                            "Foreign KEY(Appointment_ID) REFERENCES Appointment(Appointment_ID)," +
                            "Foreign KEY(Branch_ID) REFERENCES Branch(Branch_ID)" +
                            ")");
        }
        catch (Exception e)
        {
            System.out.println(e);
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
