package gui.controller;

import com.google.common.collect.Lists;
import core.Deck;
import core.Karte;
import helper.RecognitionLevelTranslator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import helper.DAO.CardDAO;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import core.enums.RecognitionLevel;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class QuizController {

    public Deck deck;
    private List<Karte> cards;
    private int index = 0;
    private boolean showingBack = false;

    @FXML
    private Label deckTitleLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label progressTextLabel;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label sideLabel;
    @FXML
    private Label cardTextLabel;
    @FXML
    private HBox ratingBox;
    @FXML
    private Button revealButton;
    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        // UI Defaults
        ratingBox.setVisible(false);
        ratingBox.setManaged(false);
        progressBar.setProgress(0.0);
    }

    /**
     * Wird von MainController aufgerufen, nachdem das Fenster geladen ist.
     */
    public void startQuizForDeck(Deck deck) throws SQLException {
        this.deck = deck;
        this.cards = sortCards(CardDAO.findByDeckId(deck.getDeckid()));
        this.index = 0;
        this.showingBack = false;

        if (cards == null || cards.isEmpty()) {
            sideLabel.setText("Info");
            cardTextLabel.setText("Keine Karten in diesem Deck.");
            infoLabel.setText("0 / 0");
            progressTextLabel.setText("0 übrig");
            progressBar.setProgress(1.0);
            revealButton.setVisible(false);
            return;
        }

        showFront();
        updateProgress();
    }

    private Karte currentCard() {
        return cards.get(index);
    }

    private void showFront() {
        showingBack = false;
        ratingBox.setVisible(false);
        ratingBox.setManaged(false);
        revealButton.setVisible(true);
        closeButton.setVisible(true);

        sideLabel.setText("Vorderseite");
        cardTextLabel.setText(currentCard().getVorderseite());
    }

    private void showBack() {
        showingBack = true;
        ratingBox.setVisible(true);
        ratingBox.setManaged(true);
        revealButton.setVisible(false);
        closeButton.setVisible(false);

        sideLabel.setText("Rückseite");
        cardTextLabel.setText(currentCard().getRueckseite());
    }

    private void nextCard() {
        index++;
        if (index >= cards.size()) {
            sideLabel.setText("Fertig");
            cardTextLabel.setText("Abfrage beendet.");
            ratingBox.setVisible(false);
            ratingBox.setManaged(false);
            revealButton.setVisible(false);
            revealButton.setManaged(false);
            closeButton.setVisible(true);
            closeButton.setLayoutX(0);
            progressBar.setProgress(1.0);
            infoLabel.setText(cards.size() + " / " + cards.size());
            progressTextLabel.setText("0 übrig");
            return;
        }
        showFront();
        updateProgress();
    }

    private void updateProgress() {
        int total = cards.size();
        int current = index + 1;
        int remaining = total - index;

        infoLabel.setText(current + " / " + total);
        progressTextLabel.setText(remaining + " übrig");

        progressBar.setProgress((double) index / total);
    }

    @FXML
    private void onReveal() {
        if (cards == null || cards.isEmpty()) return;
        if (!showingBack) showBack();
    }

    @FXML
    private void onRateBad() throws SQLException {
        rateAndNext(RecognitionLevel.BAD);
    }

    @FXML
    private void onRateOk() throws SQLException {
        rateAndNext(RecognitionLevel.OK); // falls du so ein enum hast
    }

    @FXML
    private void onRateGood() throws SQLException {
        rateAndNext(RecognitionLevel.GOOD);
    }

    @FXML
    private void onRateExcellent() throws SQLException {
        rateAndNext(RecognitionLevel.EXCELLENT);
    }

    private void rateAndNext(RecognitionLevel level) throws SQLException {
        if (cards == null || cards.isEmpty()) return;

        Karte k = currentCard();

        CardDAO.updateRecognitionLevel(k.getId(), level);

        nextCard();
    }

    @FXML
    private void onClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public List<Karte> sortCards(List<Karte> cards) {
        List<Karte> result1 = Lists.newArrayList();
        List<Karte> result2 = Lists.newArrayList();
        List<Karte> result3 = Lists.newArrayList();
        List<Karte> result4 = Lists.newArrayList();
        List<Karte> finalResult = Lists.newArrayList();

        for (Karte k : cards) {
            if (RecognitionLevelTranslator.toEnum(k.getLevel()).equals(RecognitionLevel.BAD)) {
                result1.add(k);
            }
        }
        Collections.shuffle(result1);

        for (Karte k : cards) {
            if (RecognitionLevelTranslator.toEnum(k.getLevel()).equals(RecognitionLevel.OK)) {
                result2.add(k);
            }
        }
        Collections.shuffle(result2);

        for (Karte k : cards) {
            if (RecognitionLevelTranslator.toEnum(k.getLevel()).equals(RecognitionLevel.GOOD)) {
                result3.add(k);
            }
        }
        Collections.shuffle(result3);

        for (Karte k : cards) {
            if (RecognitionLevelTranslator.toEnum(k.getLevel()).equals(RecognitionLevel.EXCELLENT)) {
                result4.add(k);
            }
        }
        Collections.shuffle(result4);

        finalResult.addAll(result1);
        finalResult.addAll(result2);
        finalResult.addAll(result3);
        finalResult.addAll(result4);
        return finalResult;
    }
}
