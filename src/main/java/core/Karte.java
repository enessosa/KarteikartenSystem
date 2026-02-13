package core;

import helper.DAO.DeckDAO;
import helper.RecognitionLevelTranslator;
import core.enums.RecognitionLevel;
import java.sql.SQLException;


public class Karte {
    private final int id;
    private final int deckid;
    private String vorderseite;
    private final String rueckseite;
    private RecognitionLevel level;

    /**
     * Konstruktor mit param, die den SQL namen ähneln.
     * @param id die karten-id
     * @param deckId deckid
     * @param vorderseite String vorderseite
     * @param rueckseite String rueckseite
     * @param level recognitionlevel
     */
    public Karte(int id, int deckId, String vorderseite, String rueckseite, RecognitionLevel level) {
        this.id = id;
        this.deckid = deckId;
        this.vorderseite = vorderseite;
        this.rueckseite = rueckseite;
        this.level = level;
    }

    public int getId() {
        return id;
    }


    public String getVorderseite() {
        return vorderseite;
    }

    public String getRueckseite() {
        return rueckseite;
    }

    public String getLevel() {
        return RecognitionLevelTranslator.toString(level);
    }

    public String getDeck() throws SQLException {
        return DeckDAO.getDeckName(deckid);
    }
}

