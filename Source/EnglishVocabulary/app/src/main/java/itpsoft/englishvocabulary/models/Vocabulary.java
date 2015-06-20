package itpsoft.englishvocabulary.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import itpsoft.englishvocabulary.databases.DbController;

/**
 * Created by luand_000 on 05/06/2015.
 */
public class Vocabulary{
    private long id;
    private int cate_id;
    private String english, vietnamese;
    private String status_sync;
    private Context context;

    public static int INSERT_FALSE = 0;
    public static int INSERT_SUCCESS = 1;
    public static int INSERT_EXITS = 2;
    public static int EDIT_FALSE = 0;
    public static int EDIT_SUCCESS = 1;
    public static int EDIT_EXITS = 2;
    public static int DELETE_FALSE = 0;
    public static int DELETE_SUCCESS = 1;

    public Vocabulary() {
    }

    public Vocabulary(long id, int cate_id, String english, String vietnamese, String status_sync) {
        this.id = id;
        this.cate_id = cate_id;
        this.english = english;
        this.vietnamese = vietnamese;
        this.status_sync = status_sync;
    }

    public Vocabulary(int cate_id, String english, String vietnamese, String status_sync) {
        this.cate_id = cate_id;
        this.english = english;
        this.vietnamese = vietnamese;
        this.status_sync = status_sync;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

    public String isStatus_sync() {
        return status_sync;
    }

    public void setStatus_sync(String status_sync) {
        this.status_sync = status_sync;
    }

    //get vocabulary by cate_id
    public ArrayList<Vocabulary> initListVocabulary(int cate_id){
        ArrayList<Vocabulary> listVocabulary = new ArrayList<Vocabulary>();
        DbController dbController = DbController.getInstance(context);

        try {
            String sql = "Select * from " + DbController.TABLE_VOCABULARY + " Where " + DbController.CATEGORIES_ID + " = " + cate_id;
            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getInt(cursor.getColumnIndex(DbController.ID_VOCABULARY));
                    int cateId = cursor.getInt(cursor.getColumnIndex(DbController.CATEGORIES_ID));
                    String english = cursor.getString(cursor.getColumnIndex(DbController.ENGLISH));
                    String vietnamese = cursor.getString(cursor.getColumnIndex(DbController.VIETNAMESE));
                    String status_sync = cursor.getString(cursor.getColumnIndex(DbController.VOCABULARY_STATUS));

                    listVocabulary.add(new Vocabulary(id, cateId, english, vietnamese, status_sync));
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return listVocabulary;
    }

    //add vocabulary
    public int addVocabulary(int idTopic, String en, String vi) {
        DbController dbController = DbController.getInstance(context);
        int result = INSERT_FALSE;
        try {
            String sql = "SELECT count(*) as 'count' FROM " + DbController.TABLE_VOCABULARY + " WHERE " + DbController.ENGLISH + " = '" + en.trim() + "';";
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
                values.put(DbController.CATEGORIES_ID, idTopic);
                values.put(DbController.ENGLISH, en.trim());
                values.put(DbController.VIETNAMESE, vi.trim());
                values.put(DbController.VOCABULARY_STATUS, "0");
                dbController.insert(DbController.TABLE_VOCABULARY, null, values);
                result = INSERT_SUCCESS;
            }
        } catch (Exception e) {
            result = INSERT_FALSE;
            e.printStackTrace();
        }
        return result;
    }

    //update
    public int updateVocabulary(long idVoca, String en, String vi) {
        DbController dbController = DbController.getInstance(context);
        int result = EDIT_FALSE;
        try {
            String sql = "SELECT count(*) as 'count' FROM " + DbController.TABLE_VOCABULARY + " WHERE " + DbController.ENGLISH + " = '" + en.trim() + "';";
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
                values.put(DbController.ENGLISH, en.trim());
                values.put(DbController.VIETNAMESE, vi.trim());
                values.put(DbController.VOCABULARY_STATUS, "0");
                dbController.updateVocabylary(DbController.TABLE_CATEGORIES, values, DbController.CATEGORIES_NAME, new String[]{Long.toString(idVoca)});
                result = EDIT_SUCCESS;
            }
        } catch (Exception e) {
            result = EDIT_FALSE;
            e.printStackTrace();
        }
        return result;
    }

    //delete vocabulary
    public int delete(long idVoca) {
        DbController dbController = DbController.getInstance(context);
        int result = DELETE_FALSE;
        try {
            dbController.deleteVocabylaryById(new String[]{Long.toString(idVoca)});
            result = DELETE_SUCCESS;
        } catch (Exception e) {
            result = DELETE_FALSE;
            e.printStackTrace();
        }
        return result;
    }
}
