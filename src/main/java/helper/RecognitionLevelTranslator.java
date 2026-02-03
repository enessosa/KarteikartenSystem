package helper;

import core.enums.RecognitionLevel;

public class RecognitionLevelTranslator {

    public static String toString(RecognitionLevel recognitionLevel) {
        if (recognitionLevel == RecognitionLevel.BAD) {
            return "bad";
        } else if (recognitionLevel == RecognitionLevel.OK) {
            return "ok";
        } else if (recognitionLevel == RecognitionLevel.GOOD) {
            return "good";
        } else {
            return "Excellent";
        }
    }

    public static RecognitionLevel toEnum(String s) {
        if (s.equals("bad")) {
            return RecognitionLevel.BAD;
        } else if (s.equals("ok")) {
            return RecognitionLevel.OK;
        } else if (s.equals("good")) {
            return RecognitionLevel.GOOD;
        } else {
            return RecognitionLevel.EXCELLENT;
        }
    }
}
