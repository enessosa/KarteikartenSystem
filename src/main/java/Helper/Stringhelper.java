package Helper;

public class Stringhelper {

    public static String lengthCorrector(int maxlength, String s) {
        StringBuilder s1 = new StringBuilder(s);
        while (s1.length() < maxlength) {
            s1.insert(0, " ");
        }
        return s1.toString();
    }
}
