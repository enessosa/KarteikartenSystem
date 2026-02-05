package helper;

import core.enums.ChangeableCardInfos;
import core.enums.MainMenuOptions;
import core.enums.RecognitionLevel;
import core.enums.VerwaltungOptions;

import javax.swing.*;

import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class Options {

    public static ChangeableCardInfos getOptionForChange() {

        int i = JOptionPane.showOptionDialog(
                null,
                "Was wollen Sie ändern?",
                "Karteikarten",
                DEFAULT_OPTION,
                QUESTION_MESSAGE,
                null,
                ChangeableCardInfos.values(),
                ChangeableCardInfos.values()[0]
        );

        if (i == JOptionPane.CLOSED_OPTION) {
            return null;
        }

        return ChangeableCardInfos.values()[i];
    }

    public static RecognitionLevel getOptionForRecognitionLevel() {
        int i = JOptionPane.showOptionDialog(
                null,
                "Wie schwer war es?",
                "Karteikarten",
                DEFAULT_OPTION,
                QUESTION_MESSAGE,
                null,
                RecognitionLevel.values(),
                RecognitionLevel.values()[0]);

        if (i == JOptionPane.CLOSED_OPTION) {
            return null;
        }

        return RecognitionLevel.values()[i];
    }

    public static int getContinuationDecision() {
        int i = JOptionPane.showConfirmDialog(
                null,
                "Willst du fortfahren?",
                "Bestätigung",
                JOptionPane.YES_NO_OPTION);

        if (i == JOptionPane.CLOSED_OPTION) {
            return -1;
        }

        return i;
    }

    public static VerwaltungOptions getVerwaltungsOption() {
        return VerwaltungOptions.values()[JOptionPane.showOptionDialog(
                null,
                "Was wollen Sie machen?",
                "Karteikarten",
                DEFAULT_OPTION,
                QUESTION_MESSAGE,
                null,
                VerwaltungOptions.values(),
                VerwaltungOptions.values()[0])];
    }
}
