package m2b.magic2brain.com;

import android.view.View;
import android.view.Window;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.SortedMap;

/**
 * Created by socketdown on 08.12.16.
 */

public class RUtils {

    public static String[] getListified(Card[] cards){
        String[] list = new String[cards.length];
        for(int i = 0; i < cards.length; i++){
            list[i] = cards[i].getName();
        }

        return list;
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static String serializeArraylist(ArrayList arrayList) throws IOException {
        String serializedObject = "";
        // serialize the object
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = new ObjectOutputStream(bo);
        so.writeObject(arrayList);
        so.flush();
        serializedObject = bo.toString();

        return serializedObject;
    }

    public static ArrayList deserializeArraylist(String mystring) throws IOException, ClassNotFoundException {
        // deserialize the object
        byte b[] = mystring.getBytes();
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream si = new ObjectInputStream(bi);
        ArrayList obj = (ArrayList) si.readObject();

        return obj;
    }

    public static <V> SortedMap<String, V> filterPrefix(SortedMap<String,V> baseMap, String prefix) {
        if(prefix.length() > 0) {
            char nextLetter = (char) (prefix.charAt(prefix.length() -1) + 1);
            String end = prefix.substring(0, prefix.length()-1) + nextLetter;
            return baseMap.subMap(prefix, end);
        }
        return baseMap;
    }
}
