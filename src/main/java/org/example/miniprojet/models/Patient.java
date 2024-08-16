package org.example.miniprojet.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.miniprojet.Dbcontroller;
import org.example.miniprojet.controller.SessionManager;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Patient {
    public int cin;
    public String nom ;
    public String prenom;
    public String tel;
    public String sexe;

    public Patient(int cin,String nom ,String prenom,String tel,String sexe){
        this.cin = cin;
        this.nom=nom;
        this.prenom=prenom;
        this.tel=tel;
        this.sexe=sexe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }




    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public  static ObservableList<Patient> getdatapatient() throws SQLException {
    Dbcontroller.connection();
    Connection con = Dbcontroller.con;
    ObservableList<Patient> list = FXCollections.observableArrayList();
    if (con == null) {
        JOptionPane.showMessageDialog(null,"invalid connection");

    }
    String sql = "select * from patient";
    PreparedStatement prest = con.prepareStatement(sql);
    ResultSet rs = prest.executeQuery();

    while (rs.next()){
        int cin = rs.getInt("cin");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String tel = rs.getString("tel");
        String sexe = rs.getString("sexe");

        list.add(new Patient(cin, nom, prenom, tel, sexe));
    }

//    while (rs.next()){
//        //list.add(new Patient(Integer.parseInt(rs.getString("cin")),rs.getString("nom"),rs.getString("prenom"),rs.getString("tel"),rs.getString("tel")));
//        list.add(new Patient(12123,"","rs.getString()","rs.getString()","rs.getString()"));
//
//    }

    return list;
}


    public int updatePatient() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        String query = "UPDATE Patient SET nom = ?, prenom = ?, tel = ?, sexe = ? WHERE cin = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, this.nom);
            pst.setString(2, this.prenom);
            pst.setString(3, this.tel);
            pst.setString(4, this.sexe);
            pst.setInt(5, this.cin);

            int result = pst.executeUpdate();
            if (result > 0) {
                // Log the action of updating patient information
                int loggedInCIN = SessionManager.getInstance().getLoggedInCIN();
                String loggedInName = SessionManager.getInstance().getLoggedInName();
                insertLog(loggedInCIN, "(" + loggedInName + ") Updated patient ( "+this.cin+" ).");
                return 0; // Success
            } else {
                return 1; // No rows affected, possibly wrong cin
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error
        }
    }


    public static ObservableList<Integer> getPatientCINData() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        ObservableList<Integer> patientCIMs = FXCollections.observableArrayList();

        if (con == null) {
            return patientCIMs; // Return empty list if no connection
        }

        String sql = "SELECT cin FROM patient;";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int cim = rs.getInt("cin");
                patientCIMs.add(cim);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return patientCIMs;
    }


    public int ajouterpatient() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            return -1;
        }
        String sql = "INSERT INTO patient(cin, nom, prenom, sexe, tel) VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setInt(1, cin);
            prest.setString(2, nom);
            prest.setString(3, prenom);
            prest.setString(4, sexe);
            prest.setString(5, tel);

            int rowsAffected = prest.executeUpdate();
            // Check if the query was successful
            if (rowsAffected > 0) {
                int loggedInCIN = SessionManager.getInstance().getLoggedInCIN();
                String loggedInName = SessionManager.getInstance().getLoggedInName();
                insertLog(loggedInCIN,"( "+loggedInName+" ) Created a new Patient Account.");
                return 0; // Success

            } else {
                return 1; // No rows affected, something went wrong
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            throw new RuntimeException(e);
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




    public int suppatient() {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            return -1;
        }
        String sql = "DELETE FROM patient WHERE cin = ?";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setInt(1, this.cin);
            int rowsAffected = prest.executeUpdate();
            // Check if the query was successful
            if (rowsAffected > 0) {
                int loggedInCIN = SessionManager.getInstance().getLoggedInCIN();
                String loggedInName = SessionManager.getInstance().getLoggedInName();
                insertLog(loggedInCIN, "(" + loggedInName + ") Deleted patient with CIN " + this.cin);
                return 0; // Success
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            throw new RuntimeException(e);
        }
        return 1;
    }





    public  int impripatient () {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            return -1;
        }
        String sgl ="select * from patient where cin=?";
        try {
            PreparedStatement prest = con.prepareStatement(sgl);
            prest.setInt(1,this.cin);
            ResultSet rs = prest.executeQuery();
            while (rs.next()){

                //souel
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }


}
