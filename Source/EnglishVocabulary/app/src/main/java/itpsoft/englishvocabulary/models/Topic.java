package itpsoft.englishvocabulary.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import itpsoft.englishvocabulary.databases.DbController;
import itpsoft.englishvocabulary.ultils.Color;

/**
 * Created by Thanh Tu on 6/8/2015.
 */
public class Topic {
    private int id, number;
    private String color, name;
    private DbController dbController;
    public Topic(Context context) {
        dbController = DbController.getInstance(context);
    }

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

    //get all topic
    public ArrayList<Topic> getAll(){
        ArrayList<Topic> data = new ArrayList<Topic>();
        Color color = Color.instance();
        try {
            String sql = "Select * from " + DbController.TABLE_CATEGORIES;
            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(DbController.ID_CATEGORIES));
                    String name = cursor.getString(cursor.getColumnIndex(DbController.CATEGORIES_NAME));
                    //
                    String sql2 = "Select count(*) as 'count' from " + DbController.TABLE_VOCABULARY + " Where " + DbController.CATEGORIES_ID + " = " + id;
                    Cursor cursor2 = dbController.rawQuery(sql2, null);
                    int number = 0;
                    if (cursor2.moveToFirst()) {
                        do {
                            number = cursor2.getInt(cursor2.getColumnIndex("count"));
                        } while (cursor2.moveToNext());
                    }

                    data.add(new Topic(color.getColor(name), id, name, number));
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
    //insert Topic
    public boolean insert(String name){
        boolean result = false;
        try {
            ContentValues values = new ContentValues();
            values.put(DbController.CATEGORIES_NAME, name.trim());
            values.put(DbController.CATEGORIES_STATUS, "1");
            dbController.insert(DbController.TABLE_CATEGORIES, null, values);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
