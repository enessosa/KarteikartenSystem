package helper;

import com.google.common.collect.Lists;
import core.Deck;
import core.Karte;
import core.enums.RecognitionLevel;

import java.sql.ResultSet;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class CardDAO {

    public static void createCardDB() throws SQLException {
        {
            String sql = """
                    CREATE TABLE IF NOT EXISTS cards (
                        deck_id INTEGER NOT NULL,
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        vorderseite TEXT NOT NULL,
                        rueckseite TEXT NOT NULL,
                        recognitionlevel TEXT NOT NULL,
                    
                        FOREIGN KEY (deck_id) REFERENCES decks(id)
                    );
                    """;

            try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    public static void insert(int deckid, String vorderseite, String rueckseite, RecognitionLevel recognitionLevel) throws SQLException {

        String sql = "INSERT INTO cards (deck_id, vorderseite, rueckseite, recognitionlevel) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, String.format("%d", deckid));
            ps.setString(2, vorderseite);
            ps.setString(3, rueckseite);
            ps.setString(4, RecognitionLevelTranslator.toString(recognitionLevel));
            ps.executeUpdate();
        }
    }

    public static List<Karte> findByDeckId(int deckId) throws SQLException {

        List<Karte> cards = Lists.newArrayList();

        String sql = """
                SELECT id, deck_id, vorderseite, rueckseite, recognitionlevel
                FROM cards
                WHERE deck_id = ?
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setInt(1, deckId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Karte card = new Karte(
                        rs.getInt("id"),
                        rs.getInt("deck_id"),
                        rs.getString("vorderseite"),
                        rs.getString("rueckseite"),
                        RecognitionLevelTranslator.toEnum(rs.getString("recognitionlevel")));
                cards.add(card);
            }
        }

        return cards;
    }

    public static void updateRecognitionLevel(int cardId, RecognitionLevel newLevel) throws SQLException {

        String sql = """
                UPDATE cards
                SET recognitionlevel = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, RecognitionLevelTranslator.toString(newLevel));
            ps.setInt(2, cardId);
            ps.executeUpdate();
        }
    }

    public static void update(String toUpdate, int cardId, String newValue) throws SQLException {

        if (!toUpdate.equals("vorderseite") && !toUpdate.equals("rueckseite")) {
            throw new IllegalArgumentException("Ungültige Spalte: " + toUpdate);
        }

        String sql = "UPDATE cards SET " + toUpdate + " = ? WHERE id = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, newValue);
            ps.setInt(2, cardId);
            ps.executeUpdate();
        }
    }

    public static void update(String toUpdate, int cardId, int newThing) throws SQLException {

        String sql = """
                UPDATE cards
                SET ? = ?
                Where id = ?
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, toUpdate);
            ps.setInt(2, newThing);
            ps.setInt(3, cardId);

            ps.executeUpdate();
        }
    }

    public static void deleteCard(String vorderseite) throws SQLException {

        String sql = """
                DELETE FROM cards
                WHERE vorderseite = ?;
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, vorderseite);
            ps.executeUpdate();

        }
    }

    public static void deleteCardByDeckId(int deckId) throws SQLException {

        String sql = """
                DELETE FROM cards
                WHERE deck_id = ?;
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setInt(1, deckId);
            ps.executeUpdate();
        }
    }

    public static int getCardId(String vorderseite) throws SQLException {

        String sql = """
                SELECT id
                FROM cards
                WHERE vorderseite = ?
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, vorderseite);
            ResultSet rs = ps.executeQuery();

            return rs.getInt("id");
        }


    }

    public static void showCards() throws SQLException {
        String deckname = showInputDialog("Wie heist das gesuchte Deck?");
        try {
            Deck d1 = DeckDAO.findDeck(deckname);
            List<Karte> cards = CardDAO.findByDeckId(d1.getDeckid());
            StringBuilder s = new StringBuilder();
            int max = Karte.getMaxListLength(cards);
            // verbessern hier wird das nicht korrigert
            for (Karte k : cards) {
                s.append(Stringhelper.lengthCorrector(max, "" + k.getId()));
                s.append(",");
                s.append(Stringhelper.lengthCorrector(max, k.getVorderseite()));
                s.append(",");
                s.append(Stringhelper.lengthCorrector(max, k.getRueckseite()));
                s.append(",");
                s.append(Stringhelper.lengthCorrector(max, k.getRecognitionLevel()));
                s.append("\n");
            }
            showMessageDialog(null, s.toString());
        } catch (SQLException e) {
            showMessageDialog(null, "Irgendwas ist schiefgegangen...");
        }
    }
}
