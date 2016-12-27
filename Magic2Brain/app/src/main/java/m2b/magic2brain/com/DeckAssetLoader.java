package m2b.magic2brain.com;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.io.IOException;

import m2b.magic2brain.com.magic2brain.R;

/**
 * Created by roman on 10.12.2016.
 */

public class DeckAssetLoader {

    public DeckAssetLoader(){

    }

    //TODO update json reader with more informations to read out of JSON

    private Context context;

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

    public Deck[] getDeckList(){

        //TODO implement decklist loader
        return new Deck[1];
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

            if(card.has("flavor")){
                card_flavor = card.getString("flavor");
            }

            if(card.has("text")){
                card_text = card.getString("text");
            }

            if(card.has("type")){
                card_type = card.getString("type");
            }

            c.setName(card_name);
            c.setMultiverseid(Integer.parseInt(mvid_as_string));
            c.setText(card_text);
            c.setFlavor(card_flavor);
            c.setType(card_type);

            carray[i] = c;
        }

        return carray;
    }
}