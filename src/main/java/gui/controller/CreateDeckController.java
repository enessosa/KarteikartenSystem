package gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class CreateDeckController {

    @FXML private TextField deckNameField;
    @FXML private Button createButton;

    private String resultDeckName = null; // null = abgebrochen

    public String getResultDeckName() {
        return resultDeckName;
    }

    @FXML
    private void initialize() {
        createButton.disableProperty().bind(deckNameField.textProperty().isEmpty());
    }

    @FXML
    private void onCreate(ActionEvent event) {
        String name = deckNameField.getText();
        if (name != null) {
            name = name.trim();
        }

        if (name == null || name.isEmpty()) {
            return;
        }

        resultDeckName = name;
        close(event);
    }

    @FXML
    private void onCancel(ActionEvent event) {
        resultDeckName = null;
        close(event);
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
