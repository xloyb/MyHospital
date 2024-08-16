package org.example.miniprojet;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.miniprojet.models.Patient;

import javax.swing.*;

public class UpdatePatientController {

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

    private Patient patient;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (patient != null) {
            cin.setText(String.valueOf(patient.getCin()));
            nomf.setText(patient.getNom());
            prenom.setText(patient.getPrenom());
            telephone.setText(patient.getTel());
            if ("Male".equals(patient.getSexe())) {
                sm.setSelected(true);
            } else if ("Female".equals(patient.getSexe())) {
                sf.setSelected(true);
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (isInputValid()) {
            patient.setNom(nomf.getText());
            patient.setPrenom(prenom.getText());
            patient.setTel(telephone.getText());
            patient.setSexe(sm.isSelected() ? "Male" : sf.isSelected() ? "Female" : "");

            int status = patient.updatePatient();
            switch (status) {
                case 0:
                    JOptionPane.showMessageDialog(null, "Updated successfully");
                    dialogStage.close();
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "Problem encountered during update");
                    break;
                case -1:
                    JOptionPane.showMessageDialog(null, "Connection problem");
                    break;
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        // Add input validation if necessary
        return true;
    }
}
