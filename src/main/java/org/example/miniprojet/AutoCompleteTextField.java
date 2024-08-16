package org.example.miniprojet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;

public class AutoCompleteTextField extends TextField {
    private final ObservableList<Integer> entries;
    private final ListView<Integer> listView;
    private final Popup popup;

    public AutoCompleteTextField() {
        super();
        entries = FXCollections.observableArrayList();
        listView = new ListView<>(entries);
        popup = new Popup();
        popup.getContent().add(listView);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        listView.setOnMouseClicked(event -> {
            Integer selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                setText(selectedItem.toString());
                popup.hide();
            }
        });

        setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleKeyReleased(KeyEvent event) {
        if (getText().isEmpty()) {
            popup.hide();
        } else {
            ObservableList<Integer> filteredEntries = entries.filtered(entry -> entry.toString().contains(getText()));
            listView.setItems(filteredEntries);
            if (!filteredEntries.isEmpty()) {
                if (!popup.isShowing()) {
                    popup.show(this, getScene().getWindow().getX() + localToScene(getBoundsInLocal()).getMinX(),
                            getScene().getWindow().getY() + localToScene(getBoundsInLocal()).getMinY() + getHeight());
                }
            } else {
                popup.hide();
            }
        }
    }

    public void setEntries(ObservableList<Integer> entries) {
        this.entries.setAll(entries);
    }
}
