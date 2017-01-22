package m2b.magic2brain.com;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/*
This is an helper-class. All functions all static so we don't need to create an object. This class basicly loads all informations of an deck or card from an JSON-file from our assets-folder.
There's alot of low-level actions going on there.
*/
public class DeckAssetLoader {

    public DeckAssetLoader() {}

    public static Card[] getDeck(String deckname, Context context) {
        Card[] c = null;
        try {
            InputStream is = context.getAssets().open(deckname);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
            String jsonString= writer.toString();
            c = parseCardJSON(jsonString);
        } catch (Exception e){}
        return c;
    }

    public static Deck[] getDeckList(Context context) {
        Deck[] darray = null;
                try {
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
                    darray = parseDeckJSON(jsonString);
                } catch (Exception e){}
        return darray;
    }

    private static Deck[] parseDeckJSON(String json) {
        Deck[] list = null;
        try {
            JSONArray file = new JSONArray(json);

            list = new Deck[file.length()];

            for (int i = 0; i < file.length(); i++) {
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
        } catch (Exception e){}
        return list;
    }

    private static Card[] parseCardJSON(String json)  {
        Card[] carray = null;
        try {
            JSONObject deck = new JSONObject(json);
            JSONArray cards = deck.getJSONArray("cards");
            carray = new Card[cards.length()];

            for (int i = 0; i < cards.length(); i++) {
                JSONObject card = cards.getJSONObject(i);
                Card c = new Card();
                String mvid_as_string = card.getString("multiverseid");
                String card_name = card.getString("name");

                String card_flavor = "";
                String card_text = "";
                String card_type = "";
                String card_cost = "";

                if (card.has("flavor")) {
                    card_flavor = card.getString("flavor");
                }

                if (card.has("text")) {
                    card_text = card.getString("text");
                }

                if (card.has("type")) {
                    card_type = card.getString("type");
                }
                if (card.has("manaCost")) {
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
        } catch (Exception e){}
        return carray;
    }
}