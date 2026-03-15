package gui;

import helper.DAO.CardDAO;
import helper.DAO.DeckDAO;
import helper.DatabaseManager;
import javafx.application.Application;

import java.sql.SQLException;

public class Launcher {
    public static void main(String[] args) {
        try {
            DatabaseManager.getConnection();
            DeckDAO.createDeckDB();
            CardDAO.createCardDB();
        } catch (SQLException e) {
            throw new RuntimeException("Datenbank konnte nicht initialisiert werden", e);
        }
        Application.launch(GUI.class, args);
    }
}
