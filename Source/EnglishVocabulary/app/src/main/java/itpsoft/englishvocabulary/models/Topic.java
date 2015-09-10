package itpsoft.englishvocabulary.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import itpsoft.englishvocabulary.databases.DbController;
import itpsoft.englishvocabulary.ultils.Color;
import itpsoft.englishvocabulary.ultils.Log;
import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Thanh Tu on 6/8/2015.
 */
public class Topic {
    boolean isChecked;


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
    private Context context;

    public Topic(Context context) {
        dbController = DbController.getInstance(context);
    }

    public Topic(String color, int id, String name, int number) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.number = number;
    }

    public boolean equal(Topic topic){
        boolean ret = false;
        if(this.name.equals(topic.name)){
            ret = true;
        }
        return ret;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
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
                    int id = cursor.getInt(cursor.getColumnIndex(DbController.ID_CATE));
                    String name = cursor.getString(cursor.getColumnIndex(DbController.CATEGORIES_NAME));
                    name = name.replace("\"", "'");
                    //
                    String sql2 = "Select count(*) as 'count' from " + DbController.TABLE_VOCABULARY + " Where " + DbController.ID_CATE + " = " + id;
                    Cursor cursor2 = dbController.rawQuery(sql2, null);
                    int number = 0;
                    if (cursor2.moveToFirst()) {
                        do {
                            number = cursor2.getInt(cursor2.getColumnIndex("count"));
                        } while (cursor2.moveToNext());
                    }
                    String nameconvert = "";
                    String kytudau = name.substring(0, 1);
                    String kytuconlai = name.substring(1);
                    nameconvert += kytudau.toUpperCase() + kytuconlai + "";

                    data.add(new Topic(color.getColor(name), id, nameconvert, number));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    //insert Topic
    public int insert(int idCate, String name) {
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
                values.put(DbController.ID_CATE, idCate);
                values.put(DbController.CATEGORIES_NAME, name.trim());
                values.put(DbController.CATEGORIES_STATUS, "0");
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

                    String idUpdateCate = SPUtil.instance(context).get(SPUtil.KEY_CATE_UPDATE, "");
                    String id_serverCate = "" + idServerByCateId(topic.getId());
                    if(!id_serverCate.equals("-1")){
                        if(!idUpdateCate.equals("")){
                            SPUtil.instance(context).set(SPUtil.KEY_CATE_UPDATE, idUpdateCate + "," + id_serverCate);
                        }else {
                            SPUtil.instance(context).set(SPUtil.KEY_CATE_UPDATE, id_serverCate);
                        }
                    }

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

            //id_cate_delete
            String idDeleteCate = SPUtil.instance(context).get(SPUtil.KEY_CATE_DELETE, "");
            String id_serverCate = "" + idServerByCateId(topic.getId());
            if(!id_serverCate.equals("-1")){
                if(!idDeleteCate.equals("")){
                    SPUtil.instance(context).set(SPUtil.KEY_CATE_DELETE, idDeleteCate + "," + id_serverCate);
                }else {
                    SPUtil.instance(context).set(SPUtil.KEY_CATE_DELETE, id_serverCate);
                }
            }

            dbController.deleteCategoriesById(new String[]{Integer.toString(topic.getId())});

            //reset id_voca_delete
            String listId = listIdVocaOfCateId(topic.getId());
            Log.d("LuanDT", "ListId: " + listId);
            String idDelete = SPUtil.instance(context).get(SPUtil.KEY_VOCA_DELETE, "");
            if(!idDelete.equals("")){
                SPUtil.instance(context).set(SPUtil.KEY_VOCA_DELETE, idDelete + "," + listId);
            }else {
                SPUtil.instance(context).set(SPUtil.KEY_VOCA_DELETE, listId);
            }

            deleteVocaByCateId(topic.getId());
            result = DELETE_SUCCESS;
        } catch (Exception e) {
            result = DELETE_FALSE;
            e.printStackTrace();
        }
        return result;
    }

    private String listIdVocaOfCateId(int id) {
        String result = "";
        try {
            String sql = "SELECT " + DbController.ID_SERVER_VOCA + " FROM " + DbController.TABLE_VOCABULARY + " WHERE " + DbController.VOCABULARY_STATUS + " = 1" + " and " + DbController.ID_CATE + " = " + id + ";";
            Log.d("LuanDT", "SQL: " + sql);
            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    if(cursor.isFirst()){
                        result += cursor.getInt(cursor.getColumnIndex(DbController.ID_SERVER_VOCA));
                    }else {
                        result += "," + cursor.getInt(cursor.getColumnIndex(DbController.ID_SERVER_VOCA));
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }

    private void deleteVocaByCateId(int id) {
        try {
            dbController.deleteVocabylaryByIdCate(new String[] {Integer.toString(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //max id
    public int maxId() {
        int result = -1;
        try {
            String sql = "SELECT MAX(cate_id) as 'maxid' FROM " + DbController.TABLE_CATEGORIES;
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

    //id_server by cate_id
    public int idServerByCateId(long cate_id) {
        DbController dbController = DbController.getInstance(context);
        int result = -1;
        try {
            String sql = "SELECT " + DbController.ID_SERVER_CATE + " FROM " + DbController.TABLE_CATEGORIES + " where " + DbController.ID_CATE + " = " + cate_id + " and " + DbController.CATEGORIES_STATUS + " = 1";
            Log.d("LuanDT", "SQL: " + sql);
            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    result = cursor.getInt(cursor.getColumnIndex(DbController.ID_SERVER_CATE));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            result = -1;
            e.printStackTrace();
        }
        return result;
    }

    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //get topic By Id ()
    public Topic getTopicById(int Id){
        Topic topic = null;
        try {
            String sql = "SELECT * " +
                    "FROM " +DbController.TABLE_CATEGORIES +
                    " WHERE " + DbController.ID_CATE +
                    " = " + Id;

            Cursor  cursor = dbController.rawQuery(sql,null);
            if (cursor.moveToFirst()){

                do{
                    int id = cursor.getInt(cursor.getColumnIndex(DbController.ID_CATE));
                    String name = cursor.getString(cursor.getColumnIndex(DbController.CATEGORIES_NAME));
                    name = name.replace("\"", "'");
                    String nameconvert = "";
                    String kytudau = name.substring(0, 1);
                    String kytuconlai = name.substring(1);
                    nameconvert += kytudau.toUpperCase() + kytuconlai + " ";

                    topic = new  Topic(id, nameconvert);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return topic;
    }
    //get topic WHERE IN ()
    public ArrayList<Topic> getTopicByStrId(String strId) {
        ArrayList<Topic> data = new ArrayList<Topic>();
        try {
            String sql = "SELECT * " +
                    "FROM " + DbController.TABLE_CATEGORIES +
                    " WHERE " + DbController.ID_CATE +
                    " IN " + "(" + strId + ")";

            Cursor cursor = dbController.rawQuery(sql, null);
            if (cursor.moveToFirst()) {

                do {
                    int id = cursor.getInt(cursor.getColumnIndex(DbController.ID_CATE));
                    String name = cursor.getString(cursor.getColumnIndex(DbController.CATEGORIES_NAME));
                    name = name.replace("\"", "'");
                    String nameconvert = "";
                    String kytudau = name.substring(0, 1);
                    String kytuconlai = name.substring(1);
                    nameconvert += kytudau.toUpperCase() + kytuconlai + "";

                    data.add(new Topic(id, nameconvert));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
