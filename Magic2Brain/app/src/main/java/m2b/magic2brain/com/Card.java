package m2b.magic2brain.com;

public class Card {
    private int multiverseid;
    private String name;

    public Card(int multiverseid, String name){
        setMultiverseid(multiverseid);
        setName(name);
    }


    public int getMultiverseid() {
        return multiverseid;
    }

    public void setMultiverseid(int multiverseid) {
        this.multiverseid = multiverseid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
