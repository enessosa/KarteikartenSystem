package core;

import Helper.DeckDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.io.IOException;

import static javax.swing.JOptionPane.*;

public class Deck {

    private static final String URL = "jdbc:sqlite:karteikarten.db";
    private static Connection connection;

    private String deckName;
    private int deck_id;
    private String createdAt;


    /**
     * ist der Konstruktor.
     *
     * @param deckName ist der Name des Decks.
     * @throws IOException erkennt die abzufangende Abweichung.
     */
    public Deck(String deckName, int deck_id, String createdAt) throws IOException {

        deckName = deckName
                .replaceAll("[^a-z0-9_]", "_");
        if (deckName.contains("'")) {
            while (deckName.contains("'")) {
                deckName = deckName.replace("'", " ");
            }
        }
        this.deckName = deckName;
        this.createdAt = createdAt;
        this.deck_id = deck_id;

    }

    public Deck(String deckName) throws IOException, SQLException {

        deckName = deckName
                .toLowerCase()
                .replaceAll("[^a-z0-9_]", "_");
        if (deckName.contains("'")) {
            while (deckName.contains("'")) {
                deckName = deckName.replace("'", " ");
            }
        }
        this.deckName = deckName;

        DeckDAO.insert(deckName);

        Deck vorrubergehendesDeck = DeckDAO.findDeck(deckName);
        this.deck_id = vorrubergehendesDeck.deck_id;
        this.createdAt = vorrubergehendesDeck.createdAt;
    }

    public int getDeck_id() {
        return deck_id;
    }
}
