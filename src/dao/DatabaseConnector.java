package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private final String URL = "jdbc:mysql://localhost:3306/warehouse";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
