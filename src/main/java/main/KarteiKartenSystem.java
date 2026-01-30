package main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class KarteiKartenSystem {
    public static void main(String[] args) throws IOException {

        try {
            Connection conn = DatabaseManager.getConnection();

            // Tabellen anlegen (CREATE TABLE IF NOT EXISTS)
            Deck deck1 = new Deck("Spanisch");
            Deck deck2 = new Deck("Latein");

            // App starten (UI / Logik)

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DatabaseManager.close();
            } catch (SQLException ignored) {}
        }




    }
}