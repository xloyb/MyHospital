package org.example.miniprojet.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.miniprojet.Dbcontroller;
import org.example.miniprojet.controller.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Medicamant {
    int ref;
    String libelle;
    Double prix;

    public Medicamant(int ref, String libelle, Double prix) {
        this.ref = ref;
        this.prix = prix;
        this.libelle = libelle;
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

    public int ajoutermed() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            // Connection problem
            return -1;
        }
        String sql = "INSERT INTO medicament(ref, libelle, prix) VALUES (?, ?, ?);";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setInt(1, ref);
            prest.setString(2, libelle);
            prest.setDouble(3, prix);

            int rowsAffected = prest.executeUpdate();
            // Check if the query was successful
            if (rowsAffected > 0) {
                // Log the action of adding a new medication
                int loggedInCIN = SessionManager.getInstance().getLoggedInCIN();
                String loggedInName = SessionManager.getInstance().getLoggedInName();
                insertLog(loggedInCIN, "(" + loggedInName + ") Added a new medication with ref " + ref);
                return 0; // Success
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Problem encountered during addition
        return 1;
    }

    public int suppmed() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            // Connection problem
            return -1;
        }
        String sql = "DELETE FROM medicament WHERE ref=?;";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setInt(1, this.ref);

            int rowsAffected = prest.executeUpdate();
            // Check if the query was successful
            if (rowsAffected > 0) {
                // Log the action of deleting a medication
                int loggedInCIN = SessionManager.getInstance().getLoggedInCIN();
                String loggedInName = SessionManager.getInstance().getLoggedInName();
                insertLog(loggedInCIN, "(" + loggedInName + ") Deleted medication with ref " + ref);
                return 0; // Success
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Problem encountered during deletion
        return 1;
    }

    public int updatemes() {
        Dbcontroller.connection();

        Connection con = Dbcontroller.con;
        if (con == null) {
            return -1;
        }
        String sql = "UPDATE medicament SET libelle = ?, prix = ? WHERE ref = ?;";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setString(1, libelle);
            prest.setDouble(2, prix);
            prest.setInt(3, ref);

            int rowsAffected = prest.executeUpdate();
            // Check if the query was successful
            if (rowsAffected > 0) {
                return 0; // Success
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    public static ObservableList<Integer> getMedicsRefsData() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        ObservableList<Integer> medicamentRefs = FXCollections.observableArrayList();

        if (con == null) {
            return medicamentRefs; // Return empty list if no connection
        }

        String sql = "SELECT ref FROM medicament;";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int ref = rs.getInt("ref");
                medicamentRefs.add(ref);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicamentRefs;
    }


    public static ObservableList<Medicamant> getMedicsData() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        ObservableList<Medicamant> medicaments = FXCollections.observableArrayList();

        if (con == null) {
            return medicaments; // Return empty list if no connection
        }

        String sql = "SELECT * FROM medicament;";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int ref = rs.getInt("ref");
                String libelle = rs.getString("libelle");
                double prix = rs.getDouble("prix");

                Medicamant medicament = new Medicamant(ref, libelle, prix);
                medicaments.add(medicament);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicaments;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }
    public int updateMedication() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        String query = "UPDATE medicament SET libelle = ?, prix = ? WHERE ref = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, this.libelle);
            pst.setDouble(2, this.prix);
            pst.setInt(3, this.ref);

            int result = pst.executeUpdate();
            if (result > 0) {
                return 0; // Success
            } else {
                return 1; // No rows affected, possibly wrong ref
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error
        }
    }


    public List<Integer> getMedicationRefs() {
        // Implement the logic to query your data source (e.g., database)
        // and fetch the medication references
        List<Integer> medicationRefs = new ArrayList<>();

        // Example: Query the database and add medication references to the list
        try {
            Dbcontroller.connection();
            Connection con = Dbcontroller.con;
            String query = "SELECT ref FROM medicament";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int ref = rs.getInt("ref");
                    medicationRefs.add(ref);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }

        return medicationRefs;
    }


    public static int getTotalMedicaments() throws SQLException {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        String sql = "SELECT COUNT(*) AS total FROM medicament";
        PreparedStatement prest = con.prepareStatement(sql);
        ResultSet rs = prest.executeQuery();

        if (rs.next()) {
            return rs.getInt("total");
        } else {
            return 0;
        }
    }

}
