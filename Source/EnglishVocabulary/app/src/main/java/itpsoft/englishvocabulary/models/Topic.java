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
    public static int INSERT_FALSE = 0;
    public static int INSERT_SUCCESS = 1;
    public static int INSERT_EXITS = 2;
    public static int EDIT_FALSE = 0;
    public static int EDIT_SUCCESS = 1;
    public static int EDIT_EXITS = 3;
    public static int EDIT_SAME = 2;
    public static int DELETE_FALSE = 0;
    public static int DELETE_SUCCESS = 1;
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
    public ArrayList<Topic> getAll() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    //insert Topic
    public int insert(String name) {
        int result = INSERT_FALSE;
        try {
            String sql = "SELECT count(*) as 'count' FROM " + DbController.TABLE_CATEGORIES + " WHERE " + DbController.CATEGORIES_NAME + " = '" + name.trim() + "';";
            Cursor cursor = dbController.rawQuery(sql, null);
            int number = 0;
            if (cursor.moveToFirst()) {
                do {
                    number = cursor.getInt(cursor.getColumnIndex("count"));
                } while (cursor.moveToNext());
            }
            if (number > 0) {
                result = INSERT_EXITS;
            } else {
                ContentValues values = new ContentValues();
                values.put(DbController.CATEGORIES_NAME, name.trim());
                values.put(DbController.CATEGORIES_STATUS, "1");
                dbController.insert(DbController.TABLE_CATEGORIES, null, values);
                result = INSERT_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //rename topic
    public int rename(Topic topic, String name) {
        int result = EDIT_FALSE;
        if (topic.getName().equals(name)) {
            result = EDIT_SAME;
        } else {
            try {
                String sql = "SELECT count(*) as 'count' FROM " + DbController.TABLE_CATEGORIES + " WHERE " + DbController.CATEGORIES_NAME + " = '" + name.trim() + "';";
                Cursor cursor = dbController.rawQuery(sql, null);
                int number = 0;
                if (cursor.moveToFirst()) {
                    do {
                        number = cursor.getInt(cursor.getColumnIndex("count"));
                    } while (cursor.moveToNext());
                }
                if (number > 0) {
                    result = EDIT_EXITS;
                } else {
                    ContentValues values = new ContentValues();
                    values.put(DbController.CATEGORIES_NAME, name.trim());
                    dbController.updateCategories(DbController.TABLE_CATEGORIES, values, DbController.CATEGORIES_NAME, new String[]{Integer.toString(topic.getId())});
                    result = EDIT_SUCCESS;
                }
            } catch (Exception e) {
                result = EDIT_FALSE;
                e.printStackTrace();
            }
        }
        return result;
    }

    //delete topic
    public int delete(Topic topic) {
        int result = DELETE_FALSE;
        try {
            dbController.deleteCategoriesById(new String[] {Integer.toString(topic.getId())});
            result = DELETE_SUCCESS;
        } catch (Exception e) {
            result = DELETE_FALSE;
            e.printStackTrace();
        }
        return result;
    }
}
