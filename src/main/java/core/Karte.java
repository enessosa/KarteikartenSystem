package core;

import Helper.CardDAO;
import Helper.RecognitionLevelTranslator;
import java.util.Collections;
import java.util.List;
import com.google.common.collect.Lists;
import core.enums.RecognitionLevel;
import java.sql.SQLException;


public class Karte {
    private final int id;
    private int deck_id;
    private String vorderseite;
    private String rueckseite;
    private RecognitionLevel level;

    public Karte(int id, int deckId, String vorderseite, String rueckseite, RecognitionLevel level) {
        this.id = id;
        this.deck_id = deckId;
        this.vorderseite = vorderseite;
        this.rueckseite = rueckseite;
        this.level = level;
    }

    public Karte(int id, int deckId, String vorderseite, String rueckseite) {
        this.id = id;
        this.deck_id = deckId;
        this.vorderseite = vorderseite;
        this.rueckseite = rueckseite;
        this.level = RecognitionLevel.BAD;
    }

    // Getter
    public int getId() {
        return id;
    }

    public int getDeckId() {
        return deck_id;
    }

    public String getVorderseite() {
        return vorderseite;
    }

    public String getRueckseite() {
        return rueckseite;
    }

    public String getRecognitionLevel() {
        return RecognitionLevelTranslator.toString(level);
    }

    public void setRecognitionLevel(RecognitionLevel r) throws SQLException {
        this.level = r;
        CardDAO.updateRecognitionLevel(this.id, r);
    }

    public int getMaxLength() {
        String s1 = "" + deck_id;
        String s2 = RecognitionLevelTranslator.toString(level);
        String s3 = rueckseite;
        String s4 = vorderseite;

        int l1 = s1.length();
        int l2 = s2.length();
        int l3 = s3.length();
        int l4 = s4.length();

        int max1 = Math.max(l1, l2);
        int max2 = Math.max(l3, l4);

        return Math.max(max1, max2);
    }

    public static int getMaxListLength(List<Karte> k) {
        List<Integer> maxes = Lists.newArrayList();
        for (Karte ka : k) {
            int i = ka.getMaxLength();
            maxes.add(i);
        }
        Collections.sort(maxes);
        return maxes.getFirst();
    }
}

