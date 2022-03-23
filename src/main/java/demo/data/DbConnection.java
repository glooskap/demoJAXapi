package demo.data;

import demo.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    protected static Connection conn;

    protected static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //register jdbc driver
            conn = DriverManager.getConnection(Config.URL, Config.DB_USER, Config.DB_PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.out.println("catch connection init");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("catch jdbc driver");
            e.printStackTrace();
        }
        return null;
    }

    protected static void disconnect() {
        if (conn == null)
            return;
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("catch connection close");
            e.printStackTrace();
        }
    }
}
