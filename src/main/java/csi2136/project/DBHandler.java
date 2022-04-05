package csi2136.project;

import java.sql.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DBHandler {
    private static DBHandler inst;

    private final String host;
    private final String port;
    private final String name;
    private final String userName;
    private final String password;

    private DBHandler() {
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
        if(inst == null) {
            inst = new DBHandler();
        }

        return inst;
    }

    public static void main(String[] args) throws SQLException {
        DBHandler inst = getInst();

        dropTables();
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
