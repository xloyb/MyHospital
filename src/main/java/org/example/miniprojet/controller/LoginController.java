package org.example.miniprojet.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.miniprojet.controller.LoginResult;
import org.example.miniprojet.controller.SessionManager;
import org.example.miniprojet.models.Personnel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    // Method to handle login button action
    @FXML
    public void login() {
        String username = loginField.getText();
        String password = passwordField.getText();

        // Create an instance of Personnel
        Personnel personnel = new Personnel();
        // Call the veriflogin method on the instance


        LoginResult loginResult = personnel.veriflogin(username, password);

        if (loginResult.getStatusCode() == 0) {
            System.out.println("Login successful!");

//            int loggedInCIN = 12345; // Example Cin
//            String loggedInName = "xLoy"; // Example name
//            String fonction = "admin";
            //SessionManager.getInstance().setLoggedInPersonnel(loggedInCIN, loggedInName,fonction);
            System.out.println(loginResult.getPersonnel().toString());
            SessionManager.getInstance().setLoggedInPersonnel(loginResult.getPersonnel());



            // Load the new window
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/miniprojet/Views/MainView.fxml"));
                Stage stage = new Stage();
                Image icon = new Image("icon.jpg");
                stage.getIcons().add(icon);
                stage.setTitle("Hospital Management System");
                //stage.setScene(new Scene(loader.load()));
                stage.setScene(new Scene(loader.load(),1000, 800));

                stage.show();

                // Close the login window
                ((Stage) loginButton.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // Display an error message for invalid username or password
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password!");
            alert.showAndWait();
        }
    }


}
