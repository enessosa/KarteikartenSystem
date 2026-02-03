package core;

import helper.DeckDAO;
import java.sql.SQLException;
import java.io.IOException;

public class Deck {

    private final String deckName;
    private final int deckid;
    private final String createdAt;


    /**
     * ist der Konstruktor.
     * @param deckName ist der Name des Decks.
     * @throws IOException erkennt die abzufangende Abweichung.
     * @param deckid deck id
     * @param createdAt datum
     */
    public Deck(String deckName, int deckid, String createdAt) throws IOException {

        deckName = deckName
                .replaceAll("[^a-z0-9_]", "_");
        if (deckName.contains("'")) {
            while (deckName.contains("'")) {
                deckName = deckName.replace("'", " ");
            }
        }
        this.deckName = deckName;
        this.createdAt = createdAt;
        this.deckid = deckid;

    }

    /**
     * Konstruktor.
     * @param deckName der name
     * @throws SQLException die exception
     */
    public Deck(String deckName) throws SQLException {

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
        this.deckid = vorrubergehendesDeck.deckid;
        this.createdAt = vorrubergehendesDeck.createdAt;
    }

    public int getDeckid() {
        return deckid;
    }


}
