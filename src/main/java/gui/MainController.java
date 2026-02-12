package gui;

import core.KKSYSGui;
import core.Karte;
import helper.CardDAO;
import helper.DeckDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    @FXML
    TextArea newFrontArea;
    @FXML
    TextArea newBackArea;
    @FXML
    ComboBox chooseDeck;
    @FXML
    private TableView<Karte> cardsTable;

    @FXML
    private TableColumn<Karte, String> colDeck;

    @FXML
    private TableColumn<Karte, String> colFront;

    @FXML
    private TableColumn<Karte, String> colBack;

    @FXML
    private TableColumn<Karte, String> colLevel;

    @FXML
    public void initialize() throws SQLException, IOException {

        colDeck.setCellValueFactory(new PropertyValueFactory<>("deck"));
        colFront.setCellValueFactory(new PropertyValueFactory<>("vorderseite"));
        colBack.setCellValueFactory(new PropertyValueFactory<>("rueckseite"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));

        prepareCardTable();


        List<String> deckNames = DeckDAO.getAllDeckNames();

        chooseDeck.getItems().addAll(deckNames);
    }


    @FXML
    private void onAddCard() throws SQLException {
        String vorderseite = newFrontArea.getText();
        String rueckseite = newBackArea.getText();
        String deckname = chooseDeck.getValue().toString();

        KKSYSGui.erstelleKarte(vorderseite, rueckseite, deckname);

        newFrontArea.clear();
        newBackArea.clear();
        chooseDeck.getSelectionModel().clearSelection();
    }

    @FXML
    private void onExit() {
    }

    @FXML
    private void onAbout() {
    }

    @FXML
    private void onCreateDeck() {
    }

    @FXML
    private void onRenameDeck() {
    }

    @FXML
    private void onDeleteDeck() {
    }

    @FXML
    private void onDeckSearch() {
    }

    @FXML
    private void onCardSearch() {
    }

    @FXML
    private void onStartQuiz() {
    }

    @FXML
    private void onNextCard() {
    }

    @FXML
    private void onRevealAnswer() {
    }

    @FXML
    private void onRateBad() {
    }

    @FXML
    private void onRateOkay() {
    }

    @FXML
    private void onRateGood() {
    }

    @FXML
    private void onClearNewCard() {
    }

    @FXML
    private void onSave() {
    }

    @FXML
    private void onRefreshCards() throws SQLException {
        cardsTable.getItems().clear();
        cardsTable.getItems().addAll(CardDAO.findAllCards());
    }

    @FXML
    private void onEditCard() {
    }

    @FXML
    private void onDeleteCard() {
    }


    private void prepareCardTable() throws SQLException {
        List<Karte> cards = CardDAO.findAllCards();

        for (Karte card : cards) {
            cardsTable.getItems().add(card);
        }

    }
}
