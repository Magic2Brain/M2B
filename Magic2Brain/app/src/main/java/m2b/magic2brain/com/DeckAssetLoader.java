package m2b.magic2brain.com;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import m2b.magic2brain.com.magic2brain.R;

/**
 * Created by roman on 10.12.2016.
 */

public class DeckAssetLoader {

    public DeckAssetLoader(){

    }

    //TODO update json reader with more informations to read out of JSON

    private Context context;

    public String getName(String code){
        return code; //TODO: Get Name from code
    }

    public Card[] getDeck(String deckname, Context context) throws JSONException, IOException {

        this.context = context;

        InputStream is = context.getAssets().open(deckname);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();

        return parseCardJSON(jsonString);
    }

    public Deck[] getDeckList(Context context) throws IOException, JSONException {

        this.context = context;

        InputStream is = context.getAssets().open("SetList.json");
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();

        Deck[] darray = parseDeckJSON(jsonString);

        return darray;
    }

    public Card[] getAllCards(Context context) throws JSONException, IOException {

        this.context = context;

        InputStream is = context.getAssets().open("AllCards.json");
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();

        return parseAllCardJSON(jsonString);
    }

    public String getDeckName(String identifier, Context context) throws IOException, JSONException {

        InputStream is = context.getAssets().open(identifier+".json");
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();

        JSONObject jstring = new JSONObject(jsonString);
        String deckname = jstring.getString("name");

        return deckname;
    }

    private Deck[] parseDeckJSON(String json) throws JSONException {
        JSONArray file = new JSONArray(json);

        Deck list[] = new Deck[file.length()];

        for(int i = 0; i < file.length(); i++){
            JSONObject card = file.getJSONObject(i);
            Deck c = new Deck();

            String deck_name = card.getString("name");
            String deck_code = card.getString("code");
            String deck_releaseDate = card.getString("releaseDate");

            c.setName(deck_name);
            c.setCode(deck_code);
            c.setReleaseDate(deck_releaseDate);

            list[i] = c;
        }

        return list;
    }

    private Card[] parseCardJSON(String json) throws JSONException {
        JSONObject deck = new JSONObject(json);
        JSONArray cards = deck.getJSONArray("cards");

        Card carray[] = new Card[cards.length()];

        for(int i = 0; i < cards.length(); i++){
            JSONObject card = cards.getJSONObject(i);
            Card c = new Card();
            String mvid_as_string = card.getString("multiverseid");
            String card_name = card.getString("name");

            String card_flavor = "";
            String card_text = "";
            String card_type = "";
            String card_cost = "";

            if(card.has("flavor")){
                card_flavor = card.getString("flavor");
            }

            if(card.has("text")){
                card_text = card.getString("text");
            }

            if(card.has("type")){
                card_type = card.getString("type");
            }
            if(card.has("manaCost")){
                card_cost = card.getString("manaCost");
            }

            c.setName(card_name);
            c.setMultiverseid(Integer.parseInt(mvid_as_string));
            c.setText(card_text);
            c.setFlavor(card_flavor);
            c.setType(card_type);
            c.setManaCost(card_cost);

            carray[i] = c;
        }

        return carray;
    }

    private Card[] parseAllCardJSON(String json) throws JSONException {
        JSONObject cards = new JSONObject(json);
        ArrayList<Card> clist = new ArrayList<Card>();

        Iterator keys = cards.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            JSONObject card = cards.getJSONObject((String) key);

            String card_name = (String) key;
            Card c = new Card();
            //String mvid_as_string = card.getString("multiverseid");
            String mvid_as_string = "1";

            String card_flavor = "";
            String card_text = "";
            String card_type = "";
            String card_cost = "";

            if(card.has("flavor")){
                card_flavor = card.getString("flavor");
            }

            if(card.has("text")){
                card_text = card.getString("text");
            }

            if(card.has("type")){
                card_type = card.getString("type");
            }
            if(card.has("manaCost")){
                card_cost = card.getString("manaCost");
            }

            c.setName(card_name);
            c.setMultiverseid(Integer.parseInt(mvid_as_string));
            c.setText(card_text);
            c.setFlavor(card_flavor);
            c.setType(card_type);
            c.setManaCost(card_cost);

            clist.add(c);
        }

        Card[] carray = new Card[clist.size()];
        for (int i = 0; i < clist.size(); i++) {
            carray[i] = clist.get(i);
        }

        return carray;
    }
}