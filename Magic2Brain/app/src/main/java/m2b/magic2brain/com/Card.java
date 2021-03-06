package m2b.magic2brain.com;

import java.io.Serializable;

/*
This is a class to store Cards. It stores all crucial informations of a card and has the Getters and Setters for it. There are also three Constructors.
*/
public class Card implements Serializable {
    private int multiverseid;
    private String name;
    private String flavor;
    private String text;
    private String type;
    private String manacost;

    public Card(int multiverseid, String name) {
        setMultiverseid(multiverseid);
        setName(name);
        // We fill the undefined variables with something to avoid NullPointer-Errors.
        type = "UNKOWN";
        flavor = "UNKOWN";
        text = "UNKOWN";
        manacost = "NA";
    }

    public Card(String name, String flavor, String text, String type, String manacost) {
        this.type = type;
        this.name = name;
        this.flavor = flavor;
        this.text = text;
        this.manacost = manacost;
    }

    public Card() {}

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

    public String getFlavor() {
        return this.flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManaCost() {
        return manacost;
    }

    public void setManaCost(String type) {
        this.manacost = type;
    }
}
