package csi2136.project;

import com.mysql.jdbc.Driver;
import org.hibernate.cfg.NotYetImplementedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DBHandler {
    private static DBHandler inst;

    private final String host;
    private final String port;
    private final String name;
    private final String userName;
    private final String password;

    private DBHandler(){
        Map<String, String> env = System.getenv();

        host = env.get("DB_host");
        port = env.get("DB_port");
        name = env.get("DB_name");

        userName = env.get("DB_username");
        password = env.get("DB_password");

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

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
            "jdbc:mysql://"+host+":"+port+"/"+name,
                userName,
                password
        );
    }

    public ResultSet getQuery(String query) {
        throw new NotYetImplementedException();
    }
}
