package org.example.miniprojet.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.miniprojet.models.Patient;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestionPatientsController implements Initializable {

    @FXML
    private TableView<Patient> table;

    @FXML
    private TableColumn<Patient, String> tcin;

    @FXML
    private TableColumn<Patient, String> tnom;

    @FXML
    private TableColumn<Patient, String> tprenom;

    @FXML
    private TableColumn<Patient, String> ttel;

    @FXML
    private TableColumn<Patient, String> tsexe;

    @FXML
    private TextField nomf;

    @FXML
    private TextField prenom;

    @FXML
    private TextField telephone;

    @FXML
    private TextField cin;

    @FXML
    private RadioButton sm;

    @FXML
    private RadioButton sf;

    @FXML
    private ToggleGroup genderGroup;

    @FXML
    private ObservableList<Patient> listp;


    @FXML
    private Button createPDFButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        loadData();
        setupListener();
    }

    private void initializeTableColumns() {
        tcin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        tnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ttel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        tsexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));

        genderGroup = new ToggleGroup();
        sf.setToggleGroup(genderGroup);
        sm.setToggleGroup(genderGroup);
    }

    private void loadData() {
        try {
            listp = Patient.getdatapatient();
            table.setItems(listp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListener() {
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPatientDetails(newValue));
    }

    private void showPatientDetails(Patient patient) {
        if (patient != null) {
            cin.setText(String.valueOf(patient.getCin()));
            nomf.setText(patient.getNom());
            prenom.setText(patient.getPrenom());
            telephone.setText(patient.getTel());

            if ("Male".equals(patient.getSexe())) {
                sm.setSelected(true);
            } else if ("Female".equals(patient.getSexe())) {
                sf.setSelected(true);
            } else {
                genderGroup.selectToggle(null);
            }
        } else {
            clearInputFields();
        }
    }

    private void clearInputFields() {
        cin.clear();
        nomf.clear();
        prenom.clear();
        telephone.clear();
        genderGroup.selectToggle(null);
    }

    @FXML
    public void ajout() {
        String sexe = "";
        if (sf.isSelected()) {
            sexe = "Female";
        } else if (sm.isSelected()) {
            sexe = "Male";
        } else {
            JOptionPane.showMessageDialog(null, "Please Select the Gender.");
            return;
        }

        Patient p = new Patient(Integer.parseInt(cin.getText()), nomf.getText(), prenom.getText(), telephone.getText(), sexe);
        int statut = p.ajouterpatient();

        switch (statut) {
            case 0:
                JOptionPane.showMessageDialog(null, "Added successfully");
                listp.add(p);
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Problem encountered during addition");
                break;
            case -1:
                JOptionPane.showMessageDialog(null, "Connection problem");
                break;
        }
    }

    @FXML
    public void supp() {
        Patient selectedPatient = table.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            int status = selectedPatient.suppatient();
            switch (status) {
                case 0:
                    JOptionPane.showMessageDialog(null, "Patient deleted successfully");
                    listp.remove(selectedPatient);
                    clearInputFields();
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "Error deleting patient");
                    break;
                case -1:
                    JOptionPane.showMessageDialog(null, "Connection problem");
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a patient to delete");
        }
    }


    @FXML
    private void handleShowUpdatePatient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/miniprojet/Views/UpdatePatient.fxml"));
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Patient");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(table.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UpdatePatientController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            Patient selectedPatient = table.getSelectionModel().getSelectedItem();
            if (selectedPatient != null) {
                controller.setPatient(selectedPatient);
                dialogStage.showAndWait();

            } else {
                JOptionPane.showMessageDialog(null, "Please select a patient to update.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deletePatient(ActionEvent event) {
        Patient selectedPatient = table.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Patient");
            alert.setContentText("Are you sure you want to delete this patient?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int status = selectedPatient.suppatient();
                if (status == 0) {
                    listp.remove(selectedPatient);
                    JOptionPane.showMessageDialog(null, "Patient deleted successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Error deleting patient");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a patient to delete");
        }
    }
//
//    @FXML
//    private void handleCreatePDF() {
//        // Get the current stage
//        Stage stage = (Stage) createPDFButton.getScene().getWindow();
//
//        // Create a sample patient object (replace this with your actual patient object)
//        Patient patient = new Patient(123456, "John", "Doe", "123456789", "Male");
//
//        // Call the createPDF method with the patient object
//        PDFCreator.createPDF(stage, patient);
//    }

    @FXML
    private void handleCreatePDF() {
        Stage stage = (Stage) createPDFButton.getScene().getWindow();
        Patient patient = table.getSelectionModel().getSelectedItem();

        if (patient != null) {
            PDFCreator.createPDF(stage, patient);
        } else {
            JOptionPane.showMessageDialog(null, "No patient selected!");

            //System.out.println("No patient selected!");
        }
    }
}
