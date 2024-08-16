package org.example.miniprojet;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Dbcontroller implements Initializable {
    private static final String username = "root";
    private static final String password = "";
    private static final String host = "localhost";
    private static final int port = 3306;
    private static final String dbname = "projet_hopital";
    public static Connection con ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

     public static void connection(){
        try {
            // Chargement du pilote JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établissement de la connexion avec la base de données
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname, username, password);

            System.out.println(con);
        } catch (SQLException  | ClassNotFoundException e) {
            // Lancer une RuntimeException si une erreur se produit
            throw new RuntimeException("Erreur lors de la connexion à la base de données", e);
        }
    }
}




