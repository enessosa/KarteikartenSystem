package gui.controller;

import com.google.common.collect.Lists;
import core.Deck;
import core.Karte;
import helper.RecognitionLevelTranslator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private int totalCards = 0;
    private int finishedCards = 0;
    private Stage quizStage;

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
    public void setStage(Stage stage) {
        this.quizStage = stage;
        stage.setOnCloseRequest(event -> {
            event.consume(); // Standard-Schließen abfangen
            closeQuiz();
        });
    }

    public void startQuizForDeck(Deck deck) throws SQLException {
        this.deck = deck;
        this.cards = sortCards(CardDAO.findByDeckId(deck.getDeckid()));
        this.index = 0;
        this.showingBack = false;
        this.totalCards = this.cards.size();
        this.finishedCards = 0;

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
            infoLabel.setText(totalCards + " / " + totalCards);
            progressTextLabel.setText("0 übrig");
            return;
        }
        showFront();
        updateProgress();
    }

    private void updateProgress() {
        int remaining = totalCards - finishedCards;

        infoLabel.setText(finishedCards + " / " + totalCards);
        progressTextLabel.setText(remaining + " übrig");

        progressBar.setProgress((double) finishedCards / totalCards);
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

        if (level == RecognitionLevel.BAD) {
            // Karte taucht nach 2 Karten wieder auf
            int insertAt = Math.min(index + 2, cards.size());
            cards.add(insertAt, k);
        } else if (level == RecognitionLevel.OK) {
            // Karte taucht in der zweiten Hälfte der verbleibenden Karten wieder auf
            int remaining = cards.size() - index - 1;
            int insertAt = index + 1 + Math.max(1, remaining / 2);
            cards.add(Math.min(insertAt, cards.size()), k);
        } else {
            // GOOD oder EXCELLENT: Karte ist erledigt
            finishedCards++;
        }

        nextCard();
    }

    @FXML
    private void onClose(ActionEvent event) {
        closeQuiz();
    }

    private void closeQuiz() {
        if (quizStage == null) return;

        if (quizStage.isFullScreen()) {
            // Fullscreen-Exit abwarten, dann erst schließen (macOS-Animation ist asynchron)
            quizStage.fullScreenProperty().addListener((obs, wasFullScreen, isNowFullScreen) -> {
                if (!isNowFullScreen) Platform.runLater(quizStage::close);
            });
            quizStage.setFullScreen(false);
        } else {
            quizStage.close();
        }
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
