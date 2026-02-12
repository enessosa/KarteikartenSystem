package core;

import core.enums.RecognitionLevel;
import helper.CardDAO;
import helper.DeckDAO;

import javax.swing.*;
import java.sql.SQLException;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class KKSYSGui {

    public static void erstelleKarte(String vorderseite, String rueckseite, String deckname) throws SQLException {
        try {
            int i = DeckDAO.findDeck(deckname).getDeckid();
        } catch (NullPointerException e) {
            showMessageDialog(null, "Dieses Deck gibt es nicht.");
            return;
        }

        CardDAO.insert(
                DeckDAO.findDeck(deckname).getDeckid(), vorderseite, rueckseite, RecognitionLevel.BAD);

    }
}

