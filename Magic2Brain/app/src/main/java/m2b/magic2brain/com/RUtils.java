package m2b.magic2brain.com;

import android.view.View;
import android.view.Window;

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
}
