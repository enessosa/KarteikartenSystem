//package core;
//
//import core.enums.ChangeableCardInfos;
//import helper.*;
//// import com.google.common.collect.Lists;
//import core.enums.MainMenuOptions;
//import core.enums.RecognitionLevel;
//import core.enums.VerwaltungOptions;
//
//import javax.swing.*;
//import java.io.IOException;
//import java.util.List;
////import java.io.IOException;
//import java.sql.SQLException;
//
//import static javax.swing.JOptionPane.*;
//
///**
// * diese Klasse ist die Hauptdatei.
// */
//public class KarteiKartenSystem {
//
//    /**
//     * hält programm am laufen.
//     */
//    public static boolean running = true;
//
//
//    /**
//     * main datei.
//     *
//     * @param args args
//     */
//    public static void main(String[] args) {
//
//        try {
//            DatabaseManager.getConnection();
//
//            DeckDAO.createDeckDB();
//            CardDAO.createCardDB();
//
//            while (running) {
//                MainMenuOptions o = Options.mainmenu();
//                if (o == MainMenuOptions.ERSTELLEKARTE) {
//                    erstelleKarte();
//                } else if (o == MainMenuOptions.VERWALTE) {
//                    verwalte();
//                } else if (o == MainMenuOptions.FRAGEAB) {
//                    frageAb();
//                } else if (o == MainMenuOptions.ERSTELLEDECK) {
//                    boolean weiter = true;
//                    while (weiter) {
//                        String s = showInputDialog("Wie soll das Deck heißen?");
//                        Deck deck = new Deck(s);
//
//                        int result = Options.getContinuationDecision();
//                        weiter = (result == YES_OPTION);
//                    }
//                }
//
//                if (o == null) {
//                    running = false;
//                }
//            }
//
//        } catch (SQLException | IOException e) {
////            e.printStackTrace();
//        } finally {
//            try {
//                DatabaseManager.close();
//            } catch (SQLException ignored) {
//            }
//        }
//
//
//    }
//
//    public static void erstelleKarte() throws SQLException {
//        String deckname = showInputDialog("Wie heist das gesuchte Deck?");
//        boolean weiter = true;
//        while (weiter) {
//            String vorderseite = showInputDialog("was ist die Vorderseite?");
//            String rueckseite = showInputDialog("was ist die Rueckseite?");
//
//            try {
//                int i = DeckDAO.findDeck(deckname).getDeckid();
//            } catch (NullPointerException e) {
//                showMessageDialog(null, "Dieses Deck gibt es nicht.");
//                break;
//            }
//
//            CardDAO.insert(
//                    DeckDAO.findDeck(deckname).getDeckid(), vorderseite, rueckseite, RecognitionLevel.BAD);
//
//            int result = JOptionPane.showConfirmDialog(
//                    null,
//                    "Willst du fortfahren?",
//                    "Bestätigung",
//                    JOptionPane.YES_NO_OPTION
//            );
//            weiter = (result == JOptionPane.YES_OPTION);
//        }
//    }
//
//    public static void verwalte() throws SQLException, NullPointerException, IOException {
//        VerwaltungOptions o1 = Options.getVerwaltungsOption();
//        if (o1 == VerwaltungOptions.SIEHEDECK) {
//            CardDAO.showCards();
//        } else if (o1 == VerwaltungOptions.LOESCHEDECK) {
//            String name = showInputDialog("Wie heißt das deck, welches Sie löschen wollen?");
//            DeckDAO.deleteDeck(name);
//        } else if (o1 == VerwaltungOptions.AENDERKARTE) {
//            String vorderseite = showInputDialog("Was ist die Vorderseite der Karte?");
//            int id = CardDAO.getCardId(vorderseite);
//            ChangeableCardInfos o2 = Options.getOptionForChange();
//            String s = showInputDialog("Was ist der neue Wert?");
//            assert o2 != null;
//            CardDAO.update(ChangeableCardInfosTranslator.toString(o2), id, s);
//        } else if (o1 == VerwaltungOptions.LOESCHEKARTE) {
//            String vorderseite = showInputDialog(
//                    "Was ist die Vorderseite von der Karte, die Sie löschen wollen?");
//            CardDAO.deleteCard(vorderseite);
//        } else if (o1 == VerwaltungOptions.SIEHEALLEDECKS) {
//            List<Deck> decks = DeckDAO.getAllDecks();
//            DeckDAO.showDecks(decks);
//
//        }
//    }
//
//    public static void frageAb() throws SQLException {
//        // deckid finden anhand deck namen
//        String deckname = showInputDialog("Wie heist das gesuchte Deck?");
//        boolean weiter = true;
//        while (weiter) {
//            Deck deck = DeckDAO.findDeck(deckname);
//            int deckid = deck.getDeckid();
//            List<Karte> karten = CardDAO.findByDeckId(deckid);
//            for (Karte k : karten) {
//                showMessageDialog(null, k.getVorderseite());
//                showMessageDialog(null, k.getRueckseite());
//                RecognitionLevel r = Options.getOptionForRecognitionLevel();
//                k.setRecognitionLevel(r);
//            }
//            int result = Options.getContinuationDecision();
//            weiter = (result == YES_OPTION);
//        }
//    }
//}