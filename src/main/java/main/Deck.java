package main;

// import com.google.common.collect.Lists;
// import java.util.List;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Deck {

    private static final String URL = "jdbc:sqlite:karteikarten.db";
    private static Connection connection;

    private final String deckName;
    private final String createdAt;


    /**
     * ist der Konstruktor.
     * @param deckName ist der Name des Decks.
     * @throws IOException erkennt die abzufangende Abweichung.
     */
    public Deck(String deckName) throws IOException {

        deckName = deckName
                .toLowerCase()
                .replaceAll("[^a-z0-9_]", "_");
        if (deckName.contains("'")) {
            while (deckName.contains("'")) {
                deckName = deckName.replace("'", " ");
            }
        }
        this.deckName = deckName;
        createdAt = String.format("%s", LocalDate.now());
        createDeckDatabase();
        createCardDatabase(this.deckName);
        createNewDeck(deckName, createdAt);
    }

    /**
     * mit dieser Methode ist es möglich, ein neues Deck zu erstellen.
     * @param deckName ist der Name des Decks.
     * @throws IOException erkennt die abzufangende Abweichung.
     */
    public void createCardDatabase(String deckName) throws IOException {
        {
            Path path = Path.of("database-%s.sql".formatted(deckName));
            if (Files.exists(path)) {
                return;
            }
            String sql = """
                    CREATE TABLE IF NOT EXISTS %s (
                        deck_id INTEGER NOT NULL,
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        vorderseite TEXT NOT NULL,
                        rueckseite TEXT NOT NULL,
                        reckognitionlevel TEXT NOT NULL
                    
                        FOREIGN KEY (deck_id) REFERENCES decks(id)
                    );
                    """.formatted(deckName);

            if (Files.notExists(path)) {
                Files.writeString(path, sql);
            }


            System.out.println("SQL-Datei erstellt: " + path.toAbsolutePath());
        }
    }

    /**
     * mit dieser Methode soll die Datenbank erschaffen werden, die die einzelnen Decks dann beinhaltet.
     * @throws IOException erkennt die abzufangende Abweichung.
     */
    public void createDeckDatabase() throws IOException {
        Path path = Path.of("Decks.sql");
        if (Files.exists(path)) {
            return;
        }
        {
            String sql = """
                    
                    CREATE TABLE IF NOT EXISTS decks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        created_at TEXT NOT NULL
                    );
                    """;

            if (Files.notExists(path)) {
                Files.writeString(path, sql);
            }

            System.out.println("SQL-Datei erstellt: " + path.toAbsolutePath());
        }
    }

    /**
     * mit dieser Methode kann ein neues Deck erstellt werden.
     * @param deckName ist der Name des zu erstellenden Decks.
     * @param createdAt ist das Datum, an dem das Deck erstellt wurde.
     * @throws IOException erkennt die abzufangende Abweichung.
     */
    public void createNewDeck(String deckName, String createdAt) throws IOException {
        String insertSQL = """
                
                INSERT INTO Decks (Name, createdAt)
                VALUES('%s', '%s');""".formatted(deckName, createdAt);

        Path path = Path.of("Decks.sql");

        Files.writeString(
                path,
                insertSQL,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );

        System.out.printf("Deck %s zur SQL-Datei hinzugefügt!%n", deckName);
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    public static void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
