package com.salesmanagement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static String dbUrl = "jdbc:sqlite:sales_management.db";
    private static Connection connection;

    private DatabaseConnection() {
    }

    // Chỉ dùng cho unit test - trỏ sang database khác (VD: in-memory) trước khi gọi getConnection()
    public static void setDbUrl(String url) {
        closeConnection();
        dbUrl = url;
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(dbUrl);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }
}