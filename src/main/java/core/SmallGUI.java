package core;

import core.enums.MainMenuOptions;

import static javax.swing.JOptionPane.*;

import javax.swing.*;


public class SmallGUI {


    public static void gui() {


    }

    public static MainMenuOptions mainmenu() {

        MainMenuOptions o1 = MainMenuOptions.values()[JOptionPane.showOptionDialog(
                null,
                "Was wollen Sie machen?",
                "Karteikarten",
                DEFAULT_OPTION,
                QUESTION_MESSAGE,
                null,
                MainMenuOptions.values(),
                MainMenuOptions.values()[0])];

        return o1;
    }
}
