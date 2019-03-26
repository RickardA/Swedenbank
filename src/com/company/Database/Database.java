package com.company.Database;

import java.sql.*;
import java.util.HashMap;

public class Database implements Runnable {
    private static Database databasseInstance = new Database();
    private Connection databaseConnection = null;
    private HashMap<String, PreparedStatement> preparedStatements = new HashMap<>();
    private HashMap<String,CallableStatement> callableStatements = new HashMap<>();

    public static Database getInstance() {
        return databasseInstance;
    }

    private Database() {
        connectToDb();
    }

    public PreparedStatement prepareStatement(String SQLQuery) {
        PreparedStatement ps = preparedStatements.get(SQLQuery);
        if (ps == null) {
            try {
                ps = databaseConnection.prepareStatement(SQLQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ps;
    }


    public CallableStatement callableStatement(String SQLQuery) {
        CallableStatement cs = callableStatements.get(SQLQuery);
        if (cs == null) {
            try {
                cs = databaseConnection.prepareCall(SQLQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cs;
    }


    private void connectToDb() {
        try {
            databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost/swedenbank?user=root&password=password1234&serverTimezone=UTC");
            System.out.println("Database connection established");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromDB() {
        try {
            databaseConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        getInstance();
    }
}
