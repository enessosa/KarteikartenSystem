package core;

import helper.CardDAO;
import helper.DeckDAO;

import java.sql.SQLException;
import java.io.IOException;
import java.util.List;

public class Deck {

    private final String deckName;
    private final int deckid;
    private final String createdAt;
    private int cardCount;


    /**
     * ist der Konstruktor.
     *
     * @param deckName  ist der Name des Decks.
     * @param deckid    deck id
     * @param createdAt datum
     * @throws IOException erkennt die abzufangende Abweichung.
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

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    /**
     * Konstruktor.
     *
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

    public String getDeckName() {
        return deckName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getCardCount() {
        return cardCount;
    }

    public int calculateCardCount() throws SQLException {
        List<Karte> cards = CardDAO.findByDeckId(this.deckid);
        return cards.size();
    }
}
