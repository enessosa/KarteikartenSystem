package gui.controller;

import core.Deck;
import core.Karte;
import core.enums.RecognitionLevel;
import helper.DAO.CardDAO;
import helper.DAO.DeckDAO;
import helper.TxtImporter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

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
    public Button deleteDeckButton;


    @FXML
    public void initialize() throws SQLException, IOException {

        colDeck.setCellValueFactory(new PropertyValueFactory<>("deck"));
        colFront.setCellValueFactory(new PropertyValueFactory<>("vorderseite"));
        colBack.setCellValueFactory(new PropertyValueFactory<>("rueckseite"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));

        prepareCardTable();

        prepareDeckListView();


        List<String> deckNames = DeckDAO.getAllDeckNames();

        chooseDeck.getItems().addAll(deckNames);
        chooseDeckForQuiz.getItems().addAll(deckNames);

        saveButton.disableProperty().bind(
                chooseDeck.getSelectionModel().selectedItemProperty().isNull()
                        .or(newFrontArea.textProperty().isEmpty())
                        .or(newBackArea.textProperty().isEmpty())
        );

        quizButton.disableProperty().bind(
                chooseDeckForQuiz.getSelectionModel().selectedItemProperty().isNull()
        );

        deleteDeckButton.disableProperty().bind(
                deckListView.getSelectionModel().selectedItemProperty().isNull()
        );

    }


    @FXML
    private void onAddCard() throws SQLException {
        String vorderseite = newFrontArea.getText();
        String rueckseite = newBackArea.getText();
        String deckname = chooseDeck.getValue().toString();

        erstelleKarte(vorderseite, rueckseite, deckname);

        newFrontArea.clear();
        newBackArea.clear();

        onRefreshCards();
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
            quizController.setStage(stage);
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
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

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

    @FXML
    private void onImportTxt() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("TXT-Datei importieren");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Textdateien (*.txt)", "*.txt")
        );

        File file = fileChooser.showOpenDialog(cardsTable.getScene().getWindow());
        if (file == null) return;

        try {
            TxtImporter.ImportErgebnis ergebnis = TxtImporter.parse(file);

            String deckname = ergebnis.deckName();
            if (deckname == null || deckname.isEmpty()) {
                Alert error = darkAlert(Alert.AlertType.ERROR);
                error.setTitle("Kein Deck angegeben");
                error.setHeaderText(null);
                error.setContentText("Die Datei enthält keine #deck:-Zeile.");
                error.showAndWait();
                return;
            }

            // Deck anlegen falls es noch nicht existiert
            try {
                DeckDAO.findDeck(deckname);
            } catch (Exception e) {
                DeckDAO.insert(deckname);
                deckListView.getItems().clear();
                prepareDeckListView();
                refreshDeckChoice(chooseDeck);
                refreshDeckChoice(chooseDeckForQuiz);
            }

            for (TxtImporter.KartenPaar paar : ergebnis.paare()) {
                erstelleKarte(paar.vorderseite(), paar.rueckseite(), deckname);
            }

            onRefreshCards();

            String meldung = ergebnis.paare().size() + " Karte(n) in Deck \"" + deckname + "\" importiert.";
            if (ergebnis.uebersprungen() > 0) {
                meldung += "\n" + ergebnis.uebersprungen() + " Zeile(n) übersprungen (falsches Format).";
            }

            Alert info = darkAlert(Alert.AlertType.INFORMATION);
            info.setTitle("Import abgeschlossen");
            info.setHeaderText(null);
            info.setContentText(meldung);
            info.showAndWait();

        } catch (Exception e) {
            Alert error = darkAlert(Alert.AlertType.ERROR);
            error.setTitle("Fehler beim Import");
            error.setHeaderText(null);
            error.setContentText("Die Datei konnte nicht gelesen werden:\n" + e.getMessage());
            error.showAndWait();
        }
    }

    private Alert darkAlert(Alert.AlertType type) {
        Alert alert = new Alert(type);
        String css = getClass().getResource("/dark.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);
        return alert;
    }

    public static void erstelleKarte(String vorderseite, String rueckseite, String deckname) throws SQLException {
        try {
            int i = DeckDAO.findDeck(deckname).getDeckid();
        } catch (NullPointerException e) {
            return;
        }

        CardDAO.insert(
                DeckDAO.findDeck(deckname).getDeckid(), vorderseite, rueckseite, RecognitionLevel.BAD);

    }
}
