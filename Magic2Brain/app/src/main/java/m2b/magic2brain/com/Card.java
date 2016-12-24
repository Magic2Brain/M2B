package m2b.magic2brain.com;

import java.io.Serializable;

public class Card implements Serializable{
    private int multiverseid;
    private String name;
    private String flavor;
    private String text;
    private String type;

    public Card(int multiverseid, String name){
        setMultiverseid(multiverseid);
        setName(name);
    }

    public Card(){

    }


    public int getMultiverseid() {
        return multiverseid;
    }

    public void setMultiverseid(int multiverseid) {
        this.multiverseid = multiverseid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlavor(){ return this.flavor; }

    public void setFlavor(String flavor){ this.flavor = flavor; }

    public String getText(){ return this.text; }

    public void setText(String text){ this.text = text; }

    public String getType(){ return type; }

    public void setType(String type){ this.type = type; }
}
