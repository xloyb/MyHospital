package org.example.miniprojet;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.miniprojet.models.Medicamant;

public class UpdateMedicationController {

    @FXML
    private TextField refField;

    @FXML
    private TextField libelleField;

    @FXML
    private TextField prixField;

    private Medicamant selectedMedication;

    // Method to receive the selected medication from the main controller
    public void setMedication(Medicamant medication) {
        this.selectedMedication = medication;
        System.out.println("Selected Medication: " + selectedMedication); // Debugging statement

        // Call initialize method explicitly after setting the medication
        initialize();
    }

    // Method to initialize the input fields with the selected medication's data
    @FXML
    public void initialize() {
        if (selectedMedication != null) {
            // Populate the input fields with the selected medication's data
            refField.setText(String.valueOf(selectedMedication.getRef()));
            libelleField.setText(selectedMedication.getLibelle());
            prixField.setText(String.valueOf(selectedMedication.getPrix()));

            System.out.println("Medication Data Initialized: " + selectedMedication); // Debugging statement
        } else {
            System.out.println("No selected medication found."); // Debugging statement
        }
    }

    @FXML
    private void handleUpdate() {
        // Check if a medication is selected
        if (selectedMedication != null) {
            // Retrieve the updated values from the input fields
            int refValue = Integer.parseInt(refField.getText());
            String libelleValue = libelleField.getText();
            double prixValue = Double.parseDouble(prixField.getText());

            // Update the selected medication with the new values
            selectedMedication.setRef(refValue);
            selectedMedication.setLibelle(libelleValue);
            selectedMedication.setPrix(prixValue);

            // Call the update method for the selected medication
            int status = selectedMedication.updateMedication();

            // Handle the update status
            switch (status) {
                case 0:
                    System.out.println("Medication updated successfully");
                    // You can close the update window or display a success message here
                    break;
                case 1:
                    System.out.println("Error: Problem encountered during medication update");
                    // You can display an error message here
                    break;
                case -1:
                    System.out.println("Error: Connection problem");
                    // You can display an error message here
                    break;
                default:
                    System.out.println("Unknown error occurred");
                    // You can handle other error cases here
                    break;
            }
        } else {
            System.out.println("No medication selected for update");
            // You can display an error message here
        }
    }

}
