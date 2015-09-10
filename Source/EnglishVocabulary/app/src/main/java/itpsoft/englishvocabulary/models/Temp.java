package itpsoft.englishvocabulary.models;

import android.content.Context;

/**
 * Created by Do on 04/09/2015.
 */
public class Temp {
    private String value;
    private String text;
//    private ArrayList<Temp> listTemp;
    private Context context;

    public Temp(String value, String text) {
        this.value = value;
        this.text = text;
    }
    public Temp() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



}
