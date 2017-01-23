package m2b.magic2brain.com;

import java.util.ArrayList;

/*
This is very simple class. It's just here to temporary store all favorites and pass them to any other class.
*/

public final class Favorites {
    public static ArrayList<Card> favorites_mvid;
    public static void init() {
        favorites_mvid = new ArrayList<>();
    }
}
