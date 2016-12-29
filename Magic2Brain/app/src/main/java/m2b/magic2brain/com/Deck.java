package m2b.magic2brain.com;

/**
 * Created by roman on 10.12.2016.
 */

public class Deck {

    private String name;
    private String code;
    private String releaseDate;
    private String icon;

    public Deck(){

    }

    public Deck(String name, String code, String release_date, String iconUri){
        this.name = name;
        this.code = code;
        this.releaseDate = release_date;
        this.icon = iconUri;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
