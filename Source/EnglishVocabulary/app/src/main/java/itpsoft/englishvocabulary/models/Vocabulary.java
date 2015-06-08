package itpsoft.englishvocabulary.models;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import itpsoft.englishvocabulary.databases.DbController;

/**
 * Created by luand_000 on 05/06/2015.
 */
public class Vocabulary {
    private long id;
    private int cate_id;
    private String english, vietnamese;
    private String status_sync;
    private Context context;

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

    //get all vocabulary
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
}
