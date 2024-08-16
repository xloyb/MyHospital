package org.example.miniprojet.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.example.miniprojet.models.PatientMed;
import org.example.miniprojet.models.Patient;
import org.example.miniprojet.models.Medicamant;
import javafx.scene.control.Tooltip;


import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PatientMedicamentController implements Initializable {

    @FXML
    private TextField searchTextField;
    @FXML
    private TextField searchMedicRefTextField;

    @FXML
    private ComboBox<String> searchComboBox;
    @FXML
    private ComboBox<String> patientCINComboBox;

    @FXML
    private Button insertPatientMedButton;
    @FXML
    private Button deletePatientMedC;

    @FXML
    private TableView<PatientMed> patientMedTableView;
    @FXML
    private TableColumn<PatientMed, Integer> cinColumn;
    @FXML
    private TableColumn<PatientMed, Integer> refColumn2;

    private ObservableList<PatientMed> listpm;
    private final ObservableList<String> medicamentData = FXCollections.observableArrayList();
    private final ObservableList<String> patientCINData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        refColumn2.setCellValueFactory(new PropertyValueFactory<>("ref"));


        try {
            listpm = PatientMed.getPatientMedDataFromDatabase();
            patientMedTableView.setItems(listpm);

            // Setup ComboBox data
            ObservableList<Integer> medicamentRefs = Medicamant.getMedicsRefsData();
            for (Integer ref : medicamentRefs) {
                medicamentData.add(String.valueOf(ref));
            }
            setupComboBox(searchComboBox, medicamentData);

            ObservableList<Integer> patientCINs = Patient.getPatientCINData();
            for (Integer cin : patientCINs) {
                patientCINData.add(String.valueOf(cin));
            }
            setupComboBox(patientCINComboBox, patientCINData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupComboBox(ComboBox<String> comboBox, ObservableList<String> data) {
        comboBox.setTooltip(new Tooltip());
        comboBox.setItems(data);
        comboBox.setEditable(true);

        comboBox.getEditor().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            String text = comboBox.getEditor().getText();
            if (text.isEmpty()) {
                comboBox.hide();
            } else {
                ObservableList<String> filteredList = FXCollections.observableArrayList();
                for (String item : data) {
                    if (item.toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                comboBox.setItems(filteredList);
                comboBox.getEditor().setText(text);
                comboBox.show();
                if (!filteredList.isEmpty()) {
                    comboBox.getEditor().positionCaret(text.length());
                }
            }
        });

        comboBox.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                comboBox.setItems(data);
            }
        });
    }

    @FXML
    private void handleInsertPatientMed(ActionEvent event) {
        String selectedMedicamentRef = searchComboBox.getValue();
        String selectedPatientCIN = patientCINComboBox.getValue();

        if (selectedMedicamentRef == null || selectedPatientCIN == null) {
            System.out.println("Please select both Medicament and Patient CIN.");
            return;
        }

        PatientMed patientMed = new PatientMed(Integer.parseInt(selectedPatientCIN), Integer.parseInt(selectedMedicamentRef));
        int result = patientMed.addPatientMed();
        if (result == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Insertion successful!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Insertion failed.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeletePatientMed(ActionEvent event) {
        PatientMed selectedPatientMed = patientMedTableView.getSelectionModel().getSelectedItem();
        if (selectedPatientMed == null) {
            JOptionPane.showMessageDialog(null, "Please select a Patient Med to delete.");
            return;
        }

        try {
            int status = selectedPatientMed.deletePatientMed();
            switch (status) {
                case 0:
                    JOptionPane.showMessageDialog(null, "Deleted successfully");
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "Problem encountered during deletion");
                    break;
                case -1:
                    JOptionPane.showMessageDialog(null, "Connection problem");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
        }
    }
    public void DeletePatientMedC() {

        // Get the selected medication from the table view
        PatientMed selectedPatientMed = patientMedTableView.getSelectionModel().getSelectedItem();
        System.out.println("Selected Patient Med: " + selectedPatientMed);

        if (selectedPatientMed == null) {
            JOptionPane.showMessageDialog(null, "Please select a Patient Med to delete.");
            return; // Exit the method if no medication is selected
        }

        try {
            // Delete the selected Medicamant
            int status = selectedPatientMed.deletePatientMed();

            // Handle the status
            switch (status) {
                case 0:
                    JOptionPane.showMessageDialog(null, "Deleted successfully");
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "Problem encountered during deletion");
                    break;
                case -1:
                    JOptionPane.showMessageDialog(null, "Connection problem");
                    break;
            }
        } catch (Exception ex) {
            // Print the stack trace of the exception for debugging
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
        }
    }

}
