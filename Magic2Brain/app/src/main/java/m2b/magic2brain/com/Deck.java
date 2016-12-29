package m2b.magic2brain.com;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by roman on 10.12.2016.
 */

public class Deck implements Serializable /* We need to do this, so we can pass Decks with Intents */ {
    private ArrayList<Card> set;
    public Deck(){
        set = new ArrayList<>();
    }

    public Deck(String name, String release_date, String iconUri){

    }

    public void addCard(Card c){
        set.add(c);
    }

    public void removeCard(Card c){
        set.remove(c);
    }

    public void removeCard(int index){
        set.remove(index);
    }

    public Card getCard(int index){
        return set.get(index);
    }

    public int getSize(){
        return set.size();
    }

    public ArrayList<Card> getSet(){
        return set;
    }
}
