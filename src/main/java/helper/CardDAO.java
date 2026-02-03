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

public class CardDAO {

    public static void createCardDB() throws SQLException {
        {
            String sql = """
                    CREATE TABLE IF NOT EXISTS cards (
                        deck_id INTEGER NOT NULL,
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        vorderseite TEXT NOT NULL,
                        rueckseite TEXT NOT NULL,
                        reckognitionlevel TEXT NOT NULL,
                    
                        FOREIGN KEY (deck_id) REFERENCES decks(id)
                    );
                    """;

            try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    public static void insert(int deck_id, String vorderseite, String rueckseite, RecognitionLevel recognitionLevel) throws SQLException {

        String sql = "INSERT INTO cards (deck_id, vorderseite, rueckseite, reckognitionlevel) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, String.format("%d", deck_id));
            ps.setString(2, vorderseite);
            ps.setString(3, rueckseite);
            ps.setString(4, RecognitionLevelTranslator.toString(recognitionLevel));
            ps.executeUpdate();
        }
    }

    public static List<Karte> findByDeckId(int deckId) throws SQLException {

        List<Karte> cards = Lists.newArrayList();

        String sql = """
                SELECT id, deck_id, vorderseite, rueckseite, reckognitionlevel
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
                        RecognitionLevelTranslator.toEnum(rs.getString("reckognitionlevel")));
                cards.add(card);
            }
        }

        return cards;
    }

    public static void updateRecognitionLevel(int cardId, RecognitionLevel newLevel)
            throws SQLException {

        String sql = """
                UPDATE cards
                SET reckognitionlevel = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, RecognitionLevelTranslator.toString(newLevel));
            ps.setInt(2, cardId);
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

            ResultSet rs = ps.executeQuery();

        }
    }
}
