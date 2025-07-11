package com.exemple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    protected Connection con;
    
    private static final String URL = "jdbc:postgresql://localhost:5432/tracker";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public Db() throws SQLException {
        con = DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean isConnectionActive() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}