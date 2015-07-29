package itpsoft.englishvocabulary.models;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import itpsoft.englishvocabulary.databases.DbController;
import itpsoft.englishvocabulary.ultils.Log;
import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Thanh Tu on 7/23/2015.
 */
public class Question implements Serializable {
    private String english, vietnamese;
    private boolean answer;
    private ArrayList<Question> questions;
    private DbController db;

    public Question(Context context) {
        db = DbController.getInstance(context);
//        getQuestions();
    }

    public Question(String english, String vietnamese, boolean answer) {
        this.english = english;
        this.vietnamese = vietnamese;
        this.answer = answer;
    }

    public String getEnglish() {
        return english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public boolean getAnswer() {
        return answer;
    }

    public ArrayList<Question> getQuestions() {
        questions = new ArrayList<Question>();
        String sql = "SELECT * FROM " + DbController.TABLE_VOCABULARY + " ORDER BY RANDOM()";
        Cursor cursor = db.rawQuery(sql, null);
        Random r = new Random();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DbController.ID_VOCA));
                float random = r.nextFloat();
                if (random <= 0.5) {
                    String en = cursor.getString(cursor.getColumnIndex(DbController.ENGLISH));
                    String vi = cursor.getString(cursor.getColumnIndex(DbController.VIETNAMESE));
                    questions.add(new Question(en, vi, true));
                } else {
                    String en = cursor.getString(cursor.getColumnIndex(DbController.ENGLISH));
                    String vi = selectVietnamese(id);
                    questions.add(new Question(en, vi, false));
                }
            }while (cursor.moveToNext());
        }
        return questions;
    }

    private String selectVietnamese(int id) {
        String vi = "";
        String sql = "SELECT * FROM " + DbController.TABLE_VOCABULARY + " ORDER BY RANDOM() LIMIT 1;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            int id2 = cursor.getInt(cursor.getColumnIndex(DbController.ID_VOCA));
            if (id == id2) {
                vi = selectVietnamese(id);
            } else {
                vi = cursor.getString(cursor.getColumnIndex(DbController.VIETNAMESE));
            }
        }
        return vi;
    }

    public boolean answer(int position, boolean answer) {
        Question question = questions.get(position);
        if (answer == question.getAnswer())
            return true;
        else
            return false;
    }

    public int checkData(){
        int count = 0;
        String sql = "SELECT count(*) as 'count' FROM " + DbController.TABLE_VOCABULARY;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }
        return count;
    }
}
