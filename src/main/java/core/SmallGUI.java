package core;

import core.enums.MainMenuOptions;

import static javax.swing.JOptionPane.*;

import javax.swing.*;


public class SmallGUI {


    public static void gui() {


    }

    /**
     * diese Methode wurde erstellt um die main-methode etwas reiner zu halten.
     *
     * @return die gewählte option
     */
    public static MainMenuOptions mainmenu() throws ArrayIndexOutOfBoundsException {

        int result = JOptionPane.showOptionDialog(
                null,
                "Was wollen Sie machen?",
                "Karteikarten",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                MainMenuOptions.values(),
                MainMenuOptions.values()[0]
        );

        if (result == JOptionPane.CLOSED_OPTION) {
            return null;
        }

        return MainMenuOptions.values()[result];

    }
}
