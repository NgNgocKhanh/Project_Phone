package dao;

import config.IDBConfig;

import java.sql.*;

public class JDBCConnect {
    public  static Connection getJDBCConnection() {
        Connection con = null;
        String connectionUrl = "jdbc:mysql://" + IDBConfig.HOSTNAME
                + ":" + IDBConfig.PORT + "/"
                + IDBConfig.DBNAME ;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Where is your MySQL JDBC Driver?");
            return con;
        }
        try {
            con = DriverManager.getConnection(connectionUrl, IDBConfig.USERNAME, IDBConfig.PASSWORD);
        } catch (SQLException ex) {
            System.err.println("Connection Failed! Check output console");
            return con;
        }
        return con;
    }
}
