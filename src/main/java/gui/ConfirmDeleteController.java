package gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ConfirmDeleteController {

    private boolean confirmed = false;

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void onYes(javafx.event.ActionEvent event) {
        confirmed = true;
        close(event);
    }

    @FXML
    private void onNo(javafx.event.ActionEvent event) {
        confirmed = false;
        close(event);
    }

    private void close(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
