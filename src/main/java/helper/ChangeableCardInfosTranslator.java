package helper;

import core.enums.ChangeableCardInfos;

public class ChangeableCardInfosTranslator {

    public static String toString(ChangeableCardInfos cardInfo) {
        if (cardInfo == ChangeableCardInfos.VORDERSEITE) {
            return "vorderseite";
        } else if (cardInfo == ChangeableCardInfos.RUECKSEITE) {
            return "rueckseite";
        } else {
            return "recognitionlevel";
        }
    }

    public static ChangeableCardInfos toEnum(String s) {
        if (s.equals("vorderseite")) {
            return ChangeableCardInfos.VORDERSEITE;
        } else if (s.equals("rueckseite")) {
            return ChangeableCardInfos.RUECKSEITE;
        } else {
            return ChangeableCardInfos.RECOGNITIONLEVEL;
        }

    }
}
