package org.example.miniprojet.models;

import org.example.miniprojet.Dbcontroller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Logs {
    private int cin;
    private String actions;
    private Timestamp date;

    public Logs(int cin, String actions) {
        this.cin = cin;
        this.actions = actions;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }


    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }




    public int addLog() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            return -1; // Return -1 for failed connection
        }

        String sql = "INSERT INTO Logs (cin, actions) VALUES (?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, this.cin);
            pst.setString(3, this.actions);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                return 0; // Success
            } else {
                return 1; // No rows affected, something went wrong
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    public static List<Logs> getAllLogs() throws SQLException {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        List<Logs> logsList = new ArrayList<>();

        if (con == null) {
            throw new SQLException("No database connection.");
        }

        String sql = "SELECT * FROM Logs";
        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int cin = rs.getInt("cin");
                String actions = rs.getString("actions");
                Timestamp date = rs.getTimestamp("date");
                Logs log = new Logs(cin, actions);
                log.setDate(date);
                logsList.add(log);
            }
        }

        return logsList;
    }

    public static List<Logs> getLogsByCIN(int cin) throws SQLException {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        List<Logs> logsList = new ArrayList<>();

        if (con == null) {
            throw new SQLException("No database connection.");
        }

        String sql = "SELECT * FROM Logs WHERE cin = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, cin);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String actions = rs.getString("actions");
                    Timestamp date = rs.getTimestamp("date");
                    Logs log = new Logs(cin, actions);
                    log.setDate(date);
                    logsList.add(log);
                }
            }
        }

        return logsList;
    }
}
