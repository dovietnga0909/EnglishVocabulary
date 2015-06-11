package itpsoft.englishvocabulary.models;

/**
 * Created by Thanh Tu on 6/8/2015.
 */
public class Topic {
    private int id, number;
    private String color, name;

    public Topic(String color, int id, String name, int number) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.number = number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {

        return id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {

        return number;
    }
}
