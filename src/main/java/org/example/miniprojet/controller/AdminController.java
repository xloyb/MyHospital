package org.example.miniprojet.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import org.example.miniprojet.models.Personnel;
import org.example.miniprojet.models.Medicamant;

import java.sql.SQLException;
import java.util.List;

public class AdminController {

    @FXML
    private TableView<Personnel> personnelTable;
    @FXML
    private TableColumn<Personnel, String> cinColumn;
    @FXML
    private TableColumn<Personnel, String> nomColumn;
    @FXML
    private TableColumn<Personnel, String> prenomColumn;
    @FXML
    private TableColumn<Personnel, String> fonctionColumn;

    @FXML
    private TextField cinField;
    @FXML
    private TextField functionField;
    @FXML
    private Label statusLabel;
    @FXML
    private Label totalPersonnelLabel;
    @FXML
    private Label totalMedicamentsLabel;

    private ObservableList<Personnel> personnelList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        fonctionColumn.setCellValueFactory(new PropertyValueFactory<>("fonction"));

        loadPersonnelData();
        loadSummaryData();

        personnelTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cinField.setText(newValue.getCin());
            }
        });
    }

    private void loadPersonnelData() {
        try {
            List<Personnel> personnels = Personnel.getAllPersonnel();
            personnelList.setAll(personnels);
            personnelTable.setItems(personnelList);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load personnel data.");
            e.printStackTrace();
        }
    }

    private void loadSummaryData() {
        try {
            int totalPersonnel = Personnel.getTotalPersonnel();
            int totalMedicaments = Medicamant.getTotalMedicaments();

            totalPersonnelLabel.setText(String.valueOf(totalPersonnel));
            totalMedicamentsLabel.setText(String.valueOf(totalMedicaments));
        } catch (SQLException e) {
            showAlert("Error", "Failed to load summary data.");
            e.printStackTrace();
        }
    }

    @FXML
    void updateFunction(ActionEvent event) {
        String cin = cinField.getText();
        String newFunction = functionField.getText();

        if (cin.isEmpty() || newFunction.isEmpty()) {
            showAlert("Error", "CIN and Function fields cannot be empty.");
            return;
        }

        int result = Personnel.updateFunction(cin, newFunction);
        if (result == 0) {
            showAlert("Success", "Function updated successfully.");
            statusLabel.setText("Function updated successfully.");
            statusLabel.setStyle("-fx-text-fill: green;");
            loadPersonnelData();
        } else if (result == 1) {
            showAlert("Error", "User not found or no rows affected.");
            statusLabel.setText("User not found or no rows affected.");
        } else {
            showAlert("Error", "Failed to update function.");
            statusLabel.setText("Failed to update function.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
