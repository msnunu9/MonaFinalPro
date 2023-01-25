package sn.mona.monafinalpro.Data;

/**
 *
 */
public class Medecine {
    private String key, owner;
    private String name;
    private String sickness;
    private String use;
    private  String symptoms;
    private  String Ingredients;
    private String contents;
    private String image;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSickness() {
        return sickness;
    }

    public void setSickness(String sickness) {
        this.sickness = sickness;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getImage(){return image;}
    public void setImage(String image){this.image=image;}

    @Override
    public String toString() {
        return "Medecine{" +
                "key='" + key + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", sickness='" + sickness + '\'' +
                ", use='" + use + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", Ingredients='" + Ingredients + '\'' +
                ",image='"+image+'\''+
                ", contents='" + contents + '\'' +
                '}';
    }


}



