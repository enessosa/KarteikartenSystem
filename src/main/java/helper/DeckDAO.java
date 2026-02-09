package helper;

import com.google.common.collect.Lists;
import core.Deck;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class DeckDAO {

    /**
     * mit dieser Methode soll die Datenbank erschaffen werden, die die einzelnen Decks dann beinhaltet.
     *
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

    public static List<Deck> getAllDecks() throws SQLException, IOException {
        List<Deck> decks = Lists.newArrayList();

        String sql = """
                SELECT id, name, created_at
                FROM decks
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Deck deck = new Deck(
                        rs.getString("name"),
                        rs.getInt("id"),
                        rs.getString("created_at"));

                decks.add(deck);
            }
        }

        return decks;

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

            return new Deck(
                    rs.getString("name"),
                    rs.getInt("id"),
                    rs.getString("created_at")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static void deleteDeck(String name) throws SQLException {

        Deck d1 = findDeck(name);
        int deckid = d1.getDeckid();

        String sql = """
                DELETE FROM decks
                WHERE id = ?;
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {

            ps.setInt(1, deckid);
            ps.executeUpdate();

        }

        CardDAO.deleteCardByDeckId(deckid);
    }

    public static void showDecks(List<Deck> decks) throws SQLException {
        StringBuilder s = new StringBuilder();
        for (Deck d : decks) {
            s.append(d.getDeckid());
            s.append(", ");
            s.append(d.getDeckName());
            s.append(", ");
            s.append(d.getCreatedAt());
            s.append("\n");
            s.append("Karten: ");
            s.append(d.calculateCardCount());
            s.append("\n");
        }
        JOptionPane.showMessageDialog(null, s.toString());
    }


}
