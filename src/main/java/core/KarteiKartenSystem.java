package core;

import helper.CardDAO;
import helper.DatabaseManager;
import helper.DeckDAO;
import helper.Stringhelper;
// import com.google.common.collect.Lists;
import core.enums.MainMenuOptions;
import core.enums.RecognitionLevel;
import core.enums.VerwaltungOptions;

import javax.swing.*;
// import javax.xml.crypto.dsig.keyinfo.KeyName;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;
import static javax.swing.JOptionPane.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

/**
 * diese Klasse ist die Hauptdatei.
 */
public class KarteiKartenSystem {

    /**
     * hält programm am laufen.
     */
    public static boolean running = true;


    /**
     * main datei.
     * @param args args
     * @throws IOException die aufzufangende exception
     */
    public static void main(String[] args) throws IOException {

        try {
            DatabaseManager.getConnection();

            DeckDAO.createDeckDB();
            CardDAO.createCardDB();

            while (running) {
                MainMenuOptions o = SmallGUI.mainmenu();
                if (o == MainMenuOptions.ERSTELLEKARTE) {
                    String deckname = showInputDialog("Wie heist das gesuchte Deck?");
                    boolean weiter = true;
                    while (weiter) {
                        String vorderseite = showInputDialog("was ist die Vorderseite?");
                        String rueckseite = showInputDialog("was ist die Rueckseite?");

                        try {
                            int i =  DeckDAO.findDeck(deckname).getDeckid();
                        } catch (NullPointerException e) {
                            showMessageDialog(null, "Dieses Deck gibt es nicht.");
                            break;
                        }

                        CardDAO.insert(
                                DeckDAO.findDeck(deckname).getDeckid(), vorderseite, rueckseite, RecognitionLevel.BAD);

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Willst du fortfahren?",
                                "Bestätigung",
                                JOptionPane.YES_NO_OPTION
                        );
                        weiter = (result == JOptionPane.YES_OPTION);
                    }

                } else if (o == MainMenuOptions.VERWALTE) {
                    VerwaltungOptions o1 = VerwaltungOptions.values()[JOptionPane.showOptionDialog(
                            null,
                            "Was wollen Sie machen?",
                            "Karteikarten",
                            DEFAULT_OPTION,
                            QUESTION_MESSAGE,
                            null,
                            VerwaltungOptions.values(),
                            VerwaltungOptions.values()[0])];

                    if (o1 == VerwaltungOptions.SIEHEDECK) {
                        String deckname = showInputDialog("Wie heist das gesuchte Deck?");
                        try {
                            Deck d1 =  DeckDAO.findDeck(deckname);
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

                        } catch (NullPointerException e) {
                            showMessageDialog(null, "Dieses Deck gibt es nicht.");
                            break;
                        }
                    } else if (o1 == VerwaltungOptions.LOESCHEDECK) {
                        String name = showInputDialog("Wie heißt das deck, welches Sie löschen wollen?");
                        DeckDAO.deleteDeck(name);
                    } else if (o1 == VerwaltungOptions.AENDERKARTE) {

                    } else if (o1 == VerwaltungOptions.LOESCHEKARTE) {
                        String vorderseite = showInputDialog(
                                "Was ist die Vorderseite von der Karte, die Sie löschen wollen?");
                        CardDAO.deleteCard(vorderseite);
                    }



                } else if (o == MainMenuOptions.FRAGEAB) {
                    // deckid finden anhand deck namen
                    String deckname = showInputDialog("Wie heist das gesuchte Deck?");
                    boolean weiter = true;
                    while (weiter) {

                        Deck deck = DeckDAO.findDeck(deckname);
                        int deckid = deck.getDeckid();
                        List<Karte> karten = CardDAO.findByDeckId(deckid);
                        for (Karte k : karten) {

                            showMessageDialog(null, k.getVorderseite());
                            showMessageDialog(null, k.getRueckseite());
                            RecognitionLevel r = RecognitionLevel.values()[JOptionPane.showOptionDialog(
                                    null,
                                    "Wie schwer war es?",
                                    "Karteikarten",
                                    DEFAULT_OPTION,
                                    QUESTION_MESSAGE,
                                    null,
                                    RecognitionLevel.values(),
                                    RecognitionLevel.values()[0])];

                            k.setRecognitionLevel(r);
                        }
                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Willst du fortfahren?",
                                "Bestätigung",
                                JOptionPane.YES_NO_OPTION
                        );
                        weiter = (result == JOptionPane.YES_OPTION);
                    }


                } else if (o == MainMenuOptions.ERSTELLEDECK) {
                    boolean weiter = true;
                    while (weiter) {
                        String s = showInputDialog("Wie soll das Deck heißen?");
                        Deck deck = new Deck(s);

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Willst du fortfahren?",
                                "Bestätigung",
                                JOptionPane.YES_NO_OPTION
                        );
                        weiter = (result == JOptionPane.YES_OPTION);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DatabaseManager.close();
            } catch (SQLException ignored) {
            }
        }


    }
}