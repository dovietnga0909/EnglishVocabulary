package itpsoft.englishvocabulary.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import itpsoft.englishvocabulary.databases.DbController;
import itpsoft.englishvocabulary.ultils.Log;

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
            String sql = "Select * from " + DbController.TABLE_VOCABULARY + " Where " + DbController.ID_CATE + " = " + cate_id;
            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getInt(cursor.getColumnIndex(DbController.ID_VOCA));
                    int cateId = cursor.getInt(cursor.getColumnIndex(DbController.ID_CATE));
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
            String sql = "SELECT count(*) as 'count' FROM " + DbController.TABLE_VOCABULARY + " WHERE " + DbController.ENGLISH + " = '" + en.trim() + "' and " + DbController.VIETNAMESE + " = '" + vi.trim() + "';";
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
                values.put(DbController.ID_CATE, idTopic);
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
            String sql = "SELECT count(*) as 'count' FROM " + DbController.TABLE_VOCABULARY + " WHERE " + DbController.ENGLISH + " = '" + en.trim() + "' and " + DbController.VIETNAMESE + " = '" + vi.trim() + "';";
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

    //list vocabulary add sync
    public JSONArray listVocabularyAdd(){
        JSONArray array = new JSONArray();
        DbController dbController = DbController.getInstance(context);

        try {
            String sql = "Select * from " + DbController.TABLE_VOCABULARY + " Where " + DbController.VOCABULARY_STATUS + " = 0";
            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getInt(cursor.getColumnIndex(DbController.ID_VOCA));
                    int cateId = cursor.getInt(cursor.getColumnIndex(DbController.ID_CATE));
                    String english = cursor.getString(cursor.getColumnIndex(DbController.ENGLISH));
                    String vietnamese = cursor.getString(cursor.getColumnIndex(DbController.VIETNAMESE));

                    JSONObject voca = new JSONObject();

                    voca.put("table", "vocabularies");
                    //voca_id, id_clien, cate_id, english, vietnamese, user_id
                    voca.put("sql", "null,'" + id + "','" + cateId  + "','" + english  + "','" + vietnamese  + "','" + 1 + "'" );


                    array.put(voca);

                } while (cursor.moveToNext());

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //list topic add sync
        try {
            String sql = "Select * from " + DbController.TABLE_CATEGORIES + " Where " + DbController.CATEGORIES_STATUS + " = 0";
            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getInt(cursor.getColumnIndex(DbController.ID_CATE));
                    String name = cursor.getString(cursor.getColumnIndex(DbController.CATEGORIES_NAME));

                    JSONObject voca = new JSONObject();

                    voca.put("table", "categories");
                    //cate_id, id_clien, name, user_id
                    voca.put("sql", "null,'" + id + "','" + name  + "','" + 1 + "'" );


                    array.put(voca);

                } while (cursor.moveToNext());

                Log.d("LuanDT", "array topic: " + array);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return array;
    }

    //max id
    public int maxId() {
        DbController dbController = DbController.getInstance(context);
        int result = -1;
        try {
            String sql = "SELECT MAX(voca_id) as 'maxid' FROM " + DbController.TABLE_VOCABULARY;
            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    result = cursor.getInt(cursor.getColumnIndex("maxid"));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            result = -1;
            e.printStackTrace();
        }
        return result;
    }
}
