package Helper;

import com.google.common.collect.Lists;
import core.Deck;
import core.Karte;
import core.enums.MainMenuOptions;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import static javax.swing.JOptionPane.*;

public class DeckDAO {

    /**
     * mit dieser Methode soll die Datenbank erschaffen werden, die die einzelnen Decks dann beinhaltet.
     * @throws SQLException erkennt die abzufangende Abweichung.
     */
    public static void createDeckDB() throws SQLException {
        {
            String sql = """
                    CREATE TABLE IF NOT EXISTS decks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        created_at TEXT NOT NULL
                    );
                    """;

            try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    public static void insert(String name) throws SQLException {

        String sql = "INSERT INTO decks (name, created_at) VALUES (?, ?);";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, String.format("%s", LocalDate.now()));
            ps.executeUpdate();
        }
    }

    public static Deck findDeck(String name) throws SQLException {

        String sql = """
                SELECT id, name, created_at
                FROM decks
                WHERE name = ?
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            Deck deck = new Deck(
                    rs.getString("name"),
                    rs.getInt("id"),
                    rs.getString("created_at")
            );
            return deck;
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}
