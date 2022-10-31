package sn.mona.monafinalpro.Data;

/**
 *
 */
public class Medecine {
    String key,owner;
    String sickness;
    String howtouse;
    String contents;
    String image;
    int whentouse;

    public Medecine(String key, String owner, String sickness, String howtouse, String contents, String image, int whentouse) {
        this.key = key;
        this.owner = owner;
        this.sickness = sickness;
        this.howtouse = howtouse;
        this.contents = contents;
        this.image = image;
        this.whentouse = whentouse;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSickness() {
        return sickness;
    }

    public void setSickness(String sickness) {
        this.sickness = sickness;
    }

    public String getHowtouse() {
        return howtouse;
    }

    public void setHowtouse(String howtouse) {
        this.howtouse = howtouse;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getWhentouse() {
        return whentouse;
    }

    public void setWhentouse(int whentouse) {
        this.whentouse = whentouse;
    }

    @Override
    public String toString() {
        return "medecine{" +
                "key='" + key + '\'' +
                ", owner='" + owner + '\'' +
                ", sickness='" + sickness + '\'' +
                ", howtouse='" + howtouse + '\'' +
                ", contents='" + contents + '\'' +
                ", image='" + image + '\'' +
                ", whentouse=" + whentouse +
                '}';
    }
}
