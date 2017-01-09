package m2b.magic2brain.com;

import java.util.ArrayList;

/**
 * Created by socketdown on 24.12.16.
 */

public final class Favorites {
    public static ArrayList<Card> favorites_mvid;

    public static void init(){
        favorites_mvid = new ArrayList<Card>();
    }

    public static void recast(){
        ArrayList<Card> temp = favorites_mvid;
        favorites_mvid = null;
        favorites_mvid = new ArrayList<Card>();
        favorites_mvid.addAll(temp);
    }

}
