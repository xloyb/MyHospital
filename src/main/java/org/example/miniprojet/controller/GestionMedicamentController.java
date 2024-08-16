package org.example.miniprojet.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.miniprojet.models.Medicamant;
import org.example.miniprojet.models.PatientMed;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GestionMedicamentController implements Initializable {
    @FXML
    private TextField ref;
    @FXML
    private TextField libelle;
    @FXML
    private TextField prix;
    @FXML
    private TableView<Medicamant> medicamentTable;
    @FXML
    private TableColumn<Medicamant, Integer> refColumn;
    @FXML
    private TableColumn<Medicamant, String> libelleColumn;
    @FXML
    private TableColumn<Medicamant, Double> prixColumn;

    private ObservableList<Medicamant> listm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        try {
            listm = FXCollections.observableArrayList(Medicamant.getMedicsData());
            medicamentTable.setItems(listm);
        } catch (Exception e) {
            showErrorAlert("Initialization Error", "Error initializing the medication list.");
        }

        // Add listener to update text fields when a row is selected
        medicamentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ref.setText(String.valueOf(newValue.getRef()));
                libelle.setText(newValue.getLibelle());
                prix.setText(String.valueOf(newValue.getPrix()));
            }
        });
    }

    @FXML
    public void addMedication() {
        if (areFieldsEmpty()) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        try {
            Medicamant medicament = createMedicamentFromInput();
            int status = medicament.ajoutermed();

            handleStatus(status, "Added successfully", "Problem encountered during addition", "Connection problem");
            if (status == 0) {
                listm.add(medicament);
                clearMedicamentInputFields();
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid input for reference or price.");
        } catch (Exception ex) {
            showErrorAlert("Addition Error", "An error occurred while adding the medication: " + ex.getMessage());
        }
    }

    @FXML
    public void deleteMedication() {
        Medicamant selectedMedication = medicamentTable.getSelectionModel().getSelectedItem();
        if (selectedMedication == null) {
            showAlert("Selection Error", "Please select a medication to delete.");
            return;
        }

        try {
            int status = selectedMedication.suppmed();

            handleStatus(status, "Deleted successfully", "Problem encountered during deletion", "Connection problem");
            if (status == 0) {
                listm.remove(selectedMedication);
            }
        } catch (Exception ex) {
            showErrorAlert("Deletion Error", "An error occurred while deleting the medication: " + ex.getMessage());
        }
    }

    @FXML
    public void updateMedication() {
        Medicamant selectedMedication = medicamentTable.getSelectionModel().getSelectedItem();
        if (selectedMedication == null) {
            showAlert("Selection Error", "Please select a medication to update.");
            return;
        }

        try {
            selectedMedication.setLibelle(libelle.getText());
            selectedMedication.setPrix(Double.parseDouble(prix.getText()));

            int status = selectedMedication.updatemes();
            handleStatus(status, "Updated successfully", "Problem encountered during update", "Connection problem");
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid input for price.");
        } catch (Exception ex) {
            showErrorAlert("Update Error", "An error occurred while updating the medication: " + ex.getMessage());
        }
    }

    @FXML
    private void handleUpdateMedication() {
        Medicamant selectedMedication = medicamentTable.getSelectionModel().getSelectedItem();
        if (selectedMedication == null) {
            showAlert("Selection Error", "Please select a medication to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/miniprojet/Views/UpdateMedication.fxml"));
            Parent root = loader.load();

            UpdateMedicationController controller = loader.getController();
            controller.setMedication(selectedMedication);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Medication");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            medicamentTable.refresh();
        } catch (IOException e) {
            showErrorAlert("Load Error", "An error occurred while loading the update view.");
        }
    }

    private boolean areFieldsEmpty() {
        return ref.getText().isEmpty() || libelle.getText().isEmpty() || prix.getText().isEmpty();
    }

    private Medicamant createMedicamentFromInput() {
        int refValue = Integer.parseInt(ref.getText());
        String libelleValue = libelle.getText();
        double prixValue = Double.parseDouble(prix.getText());
        return new Medicamant(refValue, libelleValue, prixValue);
    }

    private void clearMedicamentInputFields() {
        ref.clear();
        libelle.clear();
        prix.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleStatus(int status, String successMessage, String problemMessage, String connectionMessage) {
        switch (status) {
            case 0:
                showAlert("Success", successMessage);
                break;
            case 1:
                showAlert("Error", problemMessage);
                break;
            case -1:
                showAlert("Connection Error", connectionMessage);
                break;
            default:
                showAlert("Unknown Error", "An unknown error occurred.");
        }
    }


}
