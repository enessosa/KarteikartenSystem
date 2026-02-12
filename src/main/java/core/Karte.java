package core;

import helper.CardDAO;
import helper.DeckDAO;
import helper.RecognitionLevelTranslator;
import java.util.Collections;
import java.util.List;
import com.google.common.collect.Lists;
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

//    public Karte(int id, int deckId, String vorderseite, String rueckseite) {
//        this.id = id;
//        this.deckid = deckId;
//        this.vorderseite = vorderseite;
//        this.rueckseite = rueckseite;
//        this.level = RecognitionLevel.BAD;
//    }

    // Getter
    public int getId() {
        return id;
    }


//    public int getDeckId() {
//        return deckid;
//    }

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

    /**
     * setter.
     * @param r neu
     * @throws SQLException die Exception
     */
    public void setRecognitionLevel(RecognitionLevel r) throws SQLException {
        this.level = r;
        CardDAO.updateRecognitionLevel(this.id, r);
    }

    public void setVorderseite(String s) {
        this.vorderseite = s;

    }

    /**
     * rechnet max length der gegebenen objektvariablen aus.
     * @return maxLength
     */
    public int getMaxLength() {
        String s1 = "" + deckid;
        String s2 = RecognitionLevelTranslator.toString(level);

        int l1 = s1.length();
        int l2 = s2.length();
        int l3 = rueckseite.length();
        int l4 = vorderseite.length();

        int max1 = Math.max(l1, l2);
        int max2 = Math.max(l3, l4);

        return Math.max(max1, max2);
    }

    /**
     * rechnet max length einer liste aus.
     * @param k die liste
     * @return die max länge
     */
    public static int getMaxListLength(List<Karte> k) {
        List<Integer> maxes = Lists.newArrayList();
        for (Karte ka : k) {
            int i = ka.getMaxLength();
            maxes.add(i);
        }
        Collections.sort(maxes);
        return maxes.getLast();
    }
}

