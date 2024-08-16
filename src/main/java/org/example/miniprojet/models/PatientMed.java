package org.example.miniprojet.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.example.miniprojet.Dbcontroller;
import org.example.miniprojet.controller.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PatientMed {
    private int cin;
    private int ref;

    public PatientMed(int cin, int ref) {
        this.cin = cin;
        this.ref = ref;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public int getRef() {
        return ref;
    }


    public int addPatientMed() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            return -1; // Return -1 for failed connection
        }

        // Retrieve the logged-in CIN and Name
        int loggedInCIN = SessionManager.getInstance().getLoggedInCIN();
        String loggedInName = SessionManager.getInstance().getLoggedInName();

        String sql = "INSERT INTO patientmed (cin, ref) VALUES (?, ?)";
        try (PreparedStatement prest = con.prepareStatement(sql)) {
            prest.setInt(1, this.cin);
            prest.setInt(2, this.ref);
            int rowsAffected = prest.executeUpdate();
            if (rowsAffected > 0) {
                // Only insert log entry for successful addition
                insertLog(loggedInCIN, "Employee " + loggedInName + " added medication with reference " + this.ref + " for patient with CIN " + this.cin);
                return 0; // Success
            } else {
                return 1; // No rows affected, something went wrong
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    private void insertLog(int cin, String action) {
        Connection con = Dbcontroller.con;
        String query = "INSERT INTO Logs (cin, actions, date) VALUES (?, ?, NOW())";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, cin);
            pst.setString(2, action);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<PatientMed> getPatientMedDataFromDatabase() throws SQLException {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        ObservableList<PatientMed> patientMeds = FXCollections.observableArrayList(); // Use ObservableList

        if (con == null) {
            throw new SQLException("No database connection.");
        }

        String sql = "SELECT * FROM patientmed";
        try (PreparedStatement prest = con.prepareStatement(sql);
             ResultSet rs = prest.executeQuery()) {
            while (rs.next()) {
                int cin = rs.getInt("cin");
                int ref = rs.getInt("ref");
                patientMeds.add(new PatientMed(cin, ref));
            }
        }

        return patientMeds;
    }

    public int deletePatientMed() {
        int loggedInCIN = SessionManager.getInstance().getLoggedInCIN();
        String loggedInName = SessionManager.getInstance().getLoggedInName();

        // Get the current PatientMed's cin and ref properties
        int currentCin = this.getCin();
        int currentRef = this.getRef();
        System.out.println("currentcinn" + currentCin);
        System.out.println("currentRef" + currentRef);

        try {
            // Delete the current PatientMed
            Dbcontroller.connection();
            Connection con = Dbcontroller.con;
            if (con == null) {
                return -1; // Connection problem
            }

            String sql = "DELETE FROM patientmed WHERE cin = ? AND ref = ?";
            try (PreparedStatement prest = con.prepareStatement(sql)) {
                prest.setInt(1, currentCin);
                prest.setInt(2, currentRef);
                int rowsAffected = prest.executeUpdate();
                if (rowsAffected > 0) {
                    // Insert log entry for successful deletion
                    insertLog(loggedInCIN, "Deleted medication with reference " + currentRef + " for patient with CIN " + currentCin + " (Logged in as: " + loggedInName + ")");
                    return 0; // Success
                } else {
                    // Insert log entry for unsuccessful deletion
                    insertLog(loggedInCIN, "Failed to delete medication with reference " + currentRef + " for patient with CIN " + currentCin + " (Logged in as: " + loggedInName + ")");
                    return 1; // Problem encountered during deletion
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Insert log entry for SQL exception
            insertLog(loggedInCIN, "Error deleting medication with reference " + currentRef + " for patient with CIN " + currentCin + " (Logged in as: " + loggedInName + "): " + ex.getMessage());
            return -2; // Error
        }
    }
}


