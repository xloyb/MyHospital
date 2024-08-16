package org.example.miniprojet.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkUserFunction();

    }

    @FXML
    private StackPane contentPane;

    @FXML
    private Button createPDFButton;

    @FXML
    private Button adminControlPanelButton;

    @FXML
    private Button adminLogsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void showPatientView() throws IOException {
        loadPage("/org/example/miniprojet/Views/GestionPatients.fxml");
    }

    @FXML
    private void showMedicsView() throws IOException {
        loadPage("/org/example/miniprojet/Views/GestionMedicament.fxml");
    }

    @FXML
    private void showPatientMedicsView() throws IOException {
        loadPage("/org/example/miniprojet/Views/PatientMedicamentView.fxml");
    }

    @FXML
    private void showACP() throws IOException {
        loadPage("/org/example/miniprojet/Views/AdminDashboard.fxml");
    }

    @FXML
    private void showLogs() throws IOException {
        loadPage("/org/example/miniprojet/Views/logs_view.fxml");
    }

    @FXML
    private void createPDF() {
        Stage stage = (Stage) createPDFButton.getScene().getWindow();
//        PDFCreator.createPDF(stage);
    }

    private void checkUserFunction() {
        String userFunction = SessionManager.getInstance().getFonction();
        if ("admin".equals(userFunction)) {
            adminControlPanelButton.setVisible(true);
            adminLogsButton.setVisible(true);
//            System.out.println("now its true");


        }else{

            adminControlPanelButton.setVisible(false);
            adminLogsButton.setVisible(false);
//            System.out.println("now its false");



        }
    }


    @FXML
    private void handleLogout() {
        SessionManager.getInstance().clearSession();

        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();


    }
    private void loadPage(String fxmlFile) throws IOException {
        Node page = FXMLLoader.load(getClass().getResource(fxmlFile));
        contentPane.getChildren().setAll(page);
    }
}
