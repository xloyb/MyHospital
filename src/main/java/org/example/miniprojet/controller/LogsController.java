package org.example.miniprojet.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.miniprojet.models.Logs;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class LogsController implements Initializable {
    @FXML
    private TableView<Logs> logsTable;
    @FXML
    private TableColumn<Logs, Integer> cinColumn;
    @FXML
    private TableColumn<Logs, String> actionsColumn;
    @FXML
    private TableColumn<Logs, Timestamp> dateColumn;
    @FXML
    private TextField cinField;
    @FXML
    private TextField actionsField;
    @FXML
    private TextField dateField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up column cell value factories
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Load logs data and set it to the table
        loadLogsData();

        // Add listener for table row selection
        logsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showLogDetails(newValue));

        // Set the max width of the TextFields
        cinField.setMaxWidth(Double.MAX_VALUE);
        actionsField.setMaxWidth(Double.MAX_VALUE);
        dateField.setMaxWidth(Double.MAX_VALUE);
    }

    private void loadLogsData() {
        try {
            List<Logs> logsList = Logs.getAllLogs();
            ObservableList<Logs> logsData = FXCollections.observableArrayList(logsList);
            logsTable.setItems(logsData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }

    private void showLogDetails(Logs log) {
        if (log != null) {
            cinField.setText(String.valueOf(log.getCin()));
            actionsField.setText(log.getActions());
            dateField.setText(log.getDate().toString());
        } else {
            cinField.clear();
            actionsField.clear();
            dateField.clear();
        }
    }
}
