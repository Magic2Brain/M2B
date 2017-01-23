package m2b.magic2brain.com;

/*
This class is a helper-class. All functions are static so we don't need any objects.
 */
public class RUtils {

    public static String[] getListified(Card[] cards) { // This turns a Card-Array into a String-Array
        String[] list = new String[cards.length];
        for (int i = 0; i < cards.length; i++) {
            list[i] = cards[i].getName();
        }
        return list;
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }
}
