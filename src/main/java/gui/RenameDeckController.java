package gui;

import helper.DeckDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RenameDeckController {

    @FXML private TextField nameField;
    @FXML private Button renameButton;

    private String newName;
    private boolean renamed = false;

    public boolean isRenamed() {
        return renamed;
    }

    public String getNewName() {
        return newName;
    }

    @FXML
    private void initialize() {
        // Button nur aktiv wenn Text nicht leer
        renameButton.disableProperty().bind(
                nameField.textProperty().isEmpty()
        );
    }

    @FXML
    private void onRename(javafx.event.ActionEvent event) {
        newName = nameField.getText().trim();
        renamed = true;
        close(event);
    }

    @FXML
    private void onCancel(javafx.event.ActionEvent event) {
        renamed = false;
        close(event);
    }

    private void close(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Optional: alten Namen vorausfüllen
    public void setCurrentName(String currentName) {
        nameField.setText(currentName);
        nameField.selectAll();
    }
}