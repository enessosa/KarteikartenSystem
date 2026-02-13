package gui;

import com.google.common.collect.Lists;
import core.Deck;
import core.KKSYSGui;
import core.Karte;
import helper.CardDAO;
import helper.DeckDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainController {

    @FXML
    private TextArea newFrontArea;
    @FXML
    private TextArea newBackArea;
    @FXML
    private ComboBox chooseDeck;
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
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField cardSearchField;

    @FXML
    private ListView deckListView;

    @FXML
    private Button quizButton;

    @FXML
    public ComboBox chooseDeckForQuiz;


    @FXML
    public void initialize() throws SQLException, IOException {

        // holt die Werte ein für die Tabelle.
        colDeck.setCellValueFactory(new PropertyValueFactory<>("deck"));
        colFront.setCellValueFactory(new PropertyValueFactory<>("vorderseite"));
        colBack.setCellValueFactory(new PropertyValueFactory<>("rueckseite"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));

        prepareCardTable();

        prepareDeckListView();


        List<String> deckNames = DeckDAO.getAllDeckNames();

        // holt alle decknamen für Auswahl bei Kartenerstellung
        chooseDeck.getItems().addAll(deckNames);
        chooseDeckForQuiz.getItems().addAll(deckNames);

        // Button ist deaktiviert, solange nichts ausgewählt ist
        saveButton.disableProperty().bind(
                chooseDeck.getSelectionModel().selectedItemProperty().isNull()
                        .or(newFrontArea.textProperty().isEmpty())
                        .or(newBackArea.textProperty().isEmpty())
        );

        quizButton.disableProperty().bind(
                chooseDeckForQuiz.getSelectionModel().selectedItemProperty().isNull()
        );

    }


    @FXML
    private void onAddCard() throws SQLException {
        String vorderseite = newFrontArea.getText();
        String rueckseite = newBackArea.getText();
        String deckname = chooseDeck.getValue().toString();

        KKSYSGui.erstelleKarte(vorderseite, rueckseite, deckname);

        newFrontArea.clear();
        newBackArea.clear();

        onRefreshCards();
    }

    @FXML
    private void onExit() {
    }

    @FXML
    private void onAbout() {
    }

    @FXML
    private void onCreateDeck() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/create_deck.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            var cssUrl = getClass().getResource("/dark.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage dialog = new Stage();
            dialog.setTitle("Deck erstellen");
            dialog.setResizable(false);
            dialog.setScene(scene);

            dialog.showAndWait();

            CreateDeckController c = loader.getController();
            String deckName = c.getResultDeckName();

            if (deckName == null) {
                return;
            } else {
                DeckDAO.insert(deckName);
            }

            System.out.println("Deck erstellt: " + deckName);

            deckListView.getItems().clear();
            prepareDeckListView();
            refreshDeckChoice(chooseDeck);
            refreshDeckChoice(chooseDeckForQuiz);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRenameDeck() {
        String selectedDeckName = deckListView.getSelectionModel().getSelectedItem().toString();

        if (selectedDeckName == null) {
            System.out.println("Kein Deck ausgewählt!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rename_deck.fxml"));
            Parent root = loader.load();

            RenameDeckController controller = loader.getController();
            controller.setCurrentName(selectedDeckName);

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(
                    getClass().getResource("/dark.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setTitle("Deck umbenennen");
            stage.setResizable(false);

            stage.setOnHidden(event -> {
                if (controller.isRenamed()) {
                    String newName = controller.getNewName();

                    try {
                        DeckDAO.renameDeck(DeckDAO.findDeck(selectedDeckName).getDeckid(), newName);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        onRefreshCards();
                        refreshDeckChoice(chooseDeck);
                        refreshDeckChoice(chooseDeckForQuiz);
                        deckListView.getItems().clear();
                        prepareDeckListView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteDeck() throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/confirm_delete.fxml"));
        Parent root = loader.load();

        ConfirmDeleteController controller = loader.getController();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Bestätigung");
        stage.setResizable(false);
        stage.showAndWait();

        if (controller.isConfirmed()) {
            DeckDAO.deleteDeck(deckListView.getSelectionModel().getSelectedItem().toString());
            deckListView.getItems().clear();
            prepareDeckListView();
            onRefreshCards();
            refreshDeckChoice(chooseDeck);
            refreshDeckChoice(chooseDeckForQuiz);
        }
    }

    @FXML
    private void onDeckSearch() {
    }

    @FXML
    private void onCardSearch() throws SQLException {

        List<Karte> allCards = CardDAO.findAllCards();
        String searchText = cardSearchField.getText().toLowerCase();

        cardsTable.getItems().clear();

        for (Karte k : allCards) {
            if (k.getVorderseite().toLowerCase().contains(searchText)) {
                cardsTable.getItems().add(k);
            } else if (k.getRueckseite().toLowerCase().contains(searchText)) {
                cardsTable.getItems().add(k);
            } else if (k.getDeck().toLowerCase().contains(searchText)) {
                cardsTable.getItems().add(k);
            } else if (k.getLevel().toLowerCase().contains(searchText)) {
                cardsTable.getItems().add(k);
            }
        }
    }

    @FXML
    private void onStartQuiz() {

        String deckName = chooseDeckForQuiz.getValue().toString();
        if (deckName == null) {
            System.out.println("Kein Deck ausgewählt!");
            return;
        }

        try {
            Deck deck = DeckDAO.findDeck(deckName);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quiz_view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            URL cssUrl = getClass().getResource("/dark.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("dark.css nicht gefunden!");
            }

            Stage stage = new Stage();
            stage.setTitle("Abfrage");
            stage.setScene(scene);

            QuizController quizController = loader.getController();
            quizController.startQuizForDeck(deck);

            stage.setOnHidden(event -> {
                try {
                    onRefreshCards();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onRefreshCards() throws SQLException {
        cardsTable.getItems().clear();
        cardsTable.getItems().addAll(CardDAO.findAllCards());
    }

    @FXML
    private void onEditCard() {
        Karte selected = cardsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("Keine Karte ausgewählt!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_card.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            var cssUrl = getClass().getResource("/dark.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Karte bearbeiten");
            stage.setScene(scene);
            stage.setResizable(false);

            EditCardController c = loader.getController();
            c.setCard(selected);

            stage.setOnHidden(ev -> {
                if (c.isSaved()) {
                    // hier DB Update hast du ggf. schon im Controller gemacht
                    // dann nur refresh:
                    try {
                        onRefreshCards();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteCard() throws SQLException {
        Karte selectedCard = cardsTable.getSelectionModel().getSelectedItem();
        String vorderseite = selectedCard.getVorderseite();
        CardDAO.deleteCard(vorderseite);
        onRefreshCards();
    }


    private void prepareCardTable() throws SQLException {
        List<Karte> cards = CardDAO.findAllCards();

        for (Karte card : cards) {
            cardsTable.getItems().add(card);
        }

    }

    private void prepareDeckListView() throws SQLException, IOException {
        List<String> decknames = DeckDAO.getAllDeckNames();
        deckListView.getItems().addAll(decknames);
    }

    private void refreshDeckChoice(ComboBox box) throws SQLException {

        box.getItems().clear();
        List<String> deckNames = DeckDAO.getAllDeckNames();
        box.getItems().addAll(deckNames);

    }
}
