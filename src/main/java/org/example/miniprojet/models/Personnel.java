package org.example.miniprojet.models;

import org.example.miniprojet.Dbcontroller;
import org.example.miniprojet.controller.LoginResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Personnel {
    // Attributes
    private String cin;
    private String nom;
    private String prenom;
    private String login;
    private String password;
    private String fonction;

    // Constructor

    public Personnel() {
    }

    public Personnel(String cin, String nom, String prenom, String login, String password, String fonction) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.password = password;
        this.fonction = fonction;
    }

    // Getters and setters
    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Personnel{" +
                "cin='" + cin + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", fonction='" + fonction + '\'' +
                '}';
    }


    public LoginResult veriflogin(String username, String password) {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            return new LoginResult(-1, null); // Return -1 for invalid connection
        }
        String sgl = "select * from personnel where login=? and password = ?";
        try {
            PreparedStatement prest = con.prepareStatement(sgl);
            prest.setString(1, username);
            prest.setString(2, password);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                Personnel personnel = new Personnel(
                        rs.getString("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("fonction")
                );
                return new LoginResult(0, personnel); // Return 0 for success along with the personnel object
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new LoginResult(1, null); // Return 1 for failure
    }


//    public static int veriflogin (String username,String password){
//        Dbcontroller.connection();
//        Connection con= Dbcontroller.con;
//        if (con==null){
//            return -1;
//        }
//        String sgl ="select * from personnel where login=? and password = ?";
//        try {
//            PreparedStatement prest = con.prepareStatement(sgl);
//            prest.setString(1,username);
//            prest.setString(2,password);
//            ResultSet rs = prest.executeQuery();
//            while (rs.next()){
//                return 0;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return 1;
//    }
    public static int rember (String nom,String prenom,String fonction){
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        if (con == null) {
            return -1;
        }
        String sgl ="select * from personnel where nom=? and prenom = ? and fonction = ?";
        try {
            PreparedStatement prest = con.prepareStatement(sgl);
            prest.setString(1,nom);
            prest.setString(2,prenom);
            prest.setString(3,fonction);
            ResultSet rs = prest.executeQuery();
            while (rs.next()){
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }




    // New method to fetch all personnel
    public static List<Personnel> getAllPersonnel() throws SQLException {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        List<Personnel> personnelList = new ArrayList<>();

        String sql = "SELECT * FROM personnel";
        PreparedStatement prest = con.prepareStatement(sql);
        ResultSet rs = prest.executeQuery();

        while (rs.next()) {
            String cin = rs.getString("cin");
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            String login = rs.getString("login");
            String password = rs.getString("password");
            String fonction = rs.getString("fonction");

            Personnel personnel = new Personnel(cin, nom, prenom, login, password, fonction);
            personnelList.add(personnel);
        }

        return personnelList;
    }

    // New method to update user function
    public static int updateFunction(String cin, String newFunction) {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        String sql = "UPDATE personnel SET fonction = ? WHERE cin = ?";

        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setString(1, newFunction);
            prest.setString(2, cin);

            int rowsAffected = prest.executeUpdate();
            if (rowsAffected > 0) {
                return 0; // Success
            } else {
                return 1; // No rows affected
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    // New method to get total personnel count
    public static int getTotalPersonnel() throws SQLException {
        Dbcontroller.connection();
        Connection con = Dbcontroller.con;
        String sql = "SELECT COUNT(*) AS total FROM personnel";
        PreparedStatement prest = con.prepareStatement(sql);
        ResultSet rs = prest.executeQuery();

        if (rs.next()) {
            return rs.getInt("total");
        } else {
            return 0;
        }
    }



}
