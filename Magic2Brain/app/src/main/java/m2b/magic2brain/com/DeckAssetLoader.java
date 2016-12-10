package m2b.magic2brain.com;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.io.IOException;

/**
 * Created by roman on 10.12.2016.
 */

public class DeckAssetLoader {

    public DeckAssetLoader(){

    }

    //TODO update json reader with more informations to read out of JSON

    public Card[] getDeck(String deckname) throws JSONException {

        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, deckname+".json");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        String deckAsString = text.toString();

        return parseJSON(deckAsString);
    }

    public Card[] getPackageDeck(String name) throws UnsupportedEncodingException, JSONException {

        InputStream in = getClass().getResourceAsStream("decks/m2b/com/"+name+".json");

        String file = getStringFromInputStream(in);

        return parseJSON(file);
    }

    private Card[] parseJSON(String json) throws JSONException {
        JSONObject deck = new JSONObject(json);
        JSONArray cards = deck.getJSONArray("cards");

        Card carray[] = new Card[cards.length()];

        for(int i = 0; i < cards.length(); i++){
            JSONObject card = cards.getJSONObject(i);
            Card c = new Card();
            String mvid_as_string = card.getString("multiverseid");
            String card_name = card.getString("name");

            c.setName(card_name);
            c.setMultiverseid(Integer.parseInt(mvid_as_string));

            carray[i] = c;
        }

        return carray;
    }

    private String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}