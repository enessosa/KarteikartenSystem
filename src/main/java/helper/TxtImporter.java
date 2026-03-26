package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Liest eine .txt-Datei im Anki-Export-Format und gibt Karteikarten-Paare zurück.
 *
 * Unterstütztes Format:
 *   #separator:tab
 *   #html:true
 *   #deck:DeckName
 *   Vorderseite[TAB]Rückseite
 *
 * Zeilen die mit '#' beginnen werden als Metadaten interpretiert oder übersprungen.
 */
public class TxtImporter {

    public record KartenPaar(String vorderseite, String rueckseite) {}

    public record ImportErgebnis(String deckName, List<KartenPaar> paare, int uebersprungen) {}

    public static ImportErgebnis parse(File file) throws IOException {
        List<KartenPaar> paare = new ArrayList<>();
        int uebersprungen = 0;
        String deckName = null;
        String separator = "\t";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                if (zeile.startsWith("#")) {
                    if (zeile.startsWith("#deck:")) {
                        deckName = zeile.substring("#deck:".length()).strip();
                    } else if (zeile.startsWith("#separator:tab")) {
                        separator = "\t";
                    }
                    continue;
                }

                if (zeile.isBlank()) continue;

                String[] teile = zeile.split(separator, 2);
                if (teile.length < 2) {
                    uebersprungen++;
                    continue;
                }

                String vorderseite = teile[0].strip();
                String rueckseite = teile[1].strip();

                if (vorderseite.isEmpty() || rueckseite.isEmpty()) {
                    uebersprungen++;
                    continue;
                }

                paare.add(new KartenPaar(stripHtml(vorderseite), stripHtml(rueckseite)));
            }
        }

        return new ImportErgebnis(deckName, paare, uebersprungen);
    }

    private static String stripHtml(String text) {
        return text
                .replaceAll("(?i)<br\\s*/?>", "\n")  // <br> → Zeilenumbruch
                .replaceAll("<[^>]+>", "")             // alle anderen Tags entfernen
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
                .strip();
    }
}