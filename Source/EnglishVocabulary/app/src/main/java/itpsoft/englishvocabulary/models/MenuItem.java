package itpsoft.englishvocabulary.models;

/**
 * Created by Thanh Tu on 6/6/2015.
 */
public class MenuItem {
    private String color, title, value;
    private int icon;

    public MenuItem(String color, int icon, String title, String value) {
        this.color = color;
        this.title = title;
        this.value = value;
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public int getIcon() {
        return icon;
    }

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
