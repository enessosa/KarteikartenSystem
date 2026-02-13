package gui.controller;

import core.Deck;
import core.Karte;
import core.enums.RecognitionLevel;
import helper.DAO.CardDAO;
import helper.DAO.DeckDAO;
import helper.RecognitionLevelTranslator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class EditCardController {

    @FXML private ComboBox<Deck> deckBox;
    @FXML private ComboBox<RecognitionLevel> levelBox;
    @FXML private TextArea frontArea;
    @FXML private TextArea backArea;

    @FXML private Button saveButton;

    private Karte card;                 // die Karte, die bearbeitet wird
    private boolean saved = false;      // ob wirklich gespeichert wurde

    public boolean isSaved() {
        return saved;
    }

    public Karte getEditedCard() {
        return card; // falls du die Karte mit geänderten Werten zurückgeben willst
    }

    @FXML
    private void initialize() throws SQLException {
        // Level-Enum in ComboBox
        levelBox.getItems().setAll(
                RecognitionLevel.BAD,
                RecognitionLevel.OK,
                RecognitionLevel.GOOD,
                RecognitionLevel.EXCELLENT
        );

        // UI-Validation: Speichern nur wenn Texte nicht leer
        saveButton.disableProperty().bind(
                frontArea.textProperty().isEmpty()
                        .or(backArea.textProperty().isEmpty())
                        .or(deckBox.getSelectionModel().selectedItemProperty().isNull())
                        .or(levelBox.getSelectionModel().selectedItemProperty().isNull())
        );

        // Decks laden
        try {
            List<Deck> decks = DeckDAO.getAllDecks(); // <-- diese DAO Methode brauchst du
            deckBox.getItems().setAll(decks);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /** Wird vom MainController aufgerufen, um initiale Werte zu setzen */
    public void setCard(Karte card) throws SQLException {
        this.card = card;

        frontArea.setText(card.getVorderseite());
        backArea.setText(card.getRueckseite());

        // Level vorauswählen
        levelBox.getSelectionModel().select(RecognitionLevelTranslator.toEnum(card.getLevel()));
        deckBox.getSelectionModel().select(DeckDAO.findDeck(card.getDeck()));

        // Deck vorauswählen (je nach deinem Modell)
        // Variante A: Karte hat deckId:
        int deckId = card.getId(); // ggf. getter anpassen
        Deck selectedDeck = null;
        for (Deck d : deckBox.getItems()) {
            if (d.getDeckid() == deckId) { // ggf. getter anpassen
                selectedDeck = d;
                break;
            }
        }
        if (selectedDeck != null) {
            deckBox.getSelectionModel().select(selectedDeck);
        }
    }

    @FXML
    private void onSave(javafx.event.ActionEvent event) throws SQLException {
        // Werte aus UI lesen
        Deck newDeck = deckBox.getValue();
        RecognitionLevel newLevel = levelBox.getValue();
        String newFront = frontArea.getText().trim();
        String newBack = backArea.getText().trim();

        // In card schreiben (damit du sie zurückgeben kannst)
        CardDAO.update("vorderseite", card.getId(), newFront);
        CardDAO.update("rueckseite", card.getId(), newBack);
        CardDAO.updateRecognitionLevel(card.getId(), newLevel);
        CardDAO.updateDeck(card.getId(), newDeck.getDeckid());

        // DB Update machst du selbst (z.B. CardDAO.updateCard(card);)
        // -> wenn Update erfolgreich:
        saved = true;

        close(event);
    }

    @FXML
    private void onCancel(javafx.event.ActionEvent event) {
        saved = false;
        close(event);
    }

    private void close(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
