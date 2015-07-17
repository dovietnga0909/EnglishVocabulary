package itpsoft.englishvocabulary.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

import itpsoft.englishvocabulary.ultils.Log;

/**
 * Created by luand_000 on 03/06/2015.
 */
public class DbController extends SQLiteOpenHelper {

    //Table tbl_categories
    public static final String TABLE_CATEGORIES = "tbl_categories";
    public static final String ID_CATE = "cate_id";
    public static final String ID_SERVER_CATE = "id_server";
    public static final String CATEGORIES_NAME = "name";
    public static final String CATEGORIES_STATUS = "status_sync";
    public static final String CREATE_TABLE_CATEGORIES = "create table "
            + TABLE_CATEGORIES + " (" + ID_CATE + " integer primary key, "
            + ID_SERVER_CATE + " integer, "
            + CATEGORIES_NAME + " text not null, "
            + CATEGORIES_STATUS + " text  not null);";

    //Table tbl_vocabulary
    public static final String TABLE_VOCABULARY = "tbl_vocabulary";
    public static final String ID_VOCA = "voca_id";
    public static final String ID_SERVER_VOCA = "id_server";
    public static final String ENGLISH = "english";
    public static final String VIETNAMESE = "vietnamese";
    public static final String VOCABULARY_STATUS = "status_sync";
    public static final String CREATE_TABLE_VOCABULARY = "create table "
            + TABLE_VOCABULARY + " (" + ID_VOCA + " integer primary key, "
            + ID_CATE + " integer, "
            + ID_SERVER_VOCA + " integer, "
            + ENGLISH + " text not null, "
            + VIETNAMESE + " text not null, "
            + VOCABULARY_STATUS + " text not null);";

    private static String DB_NAME = "english_vocabulary";
    private static String DB_PATH = "/data/data/itpsoft.englishvocabulary/databases/";
    private static int DB_VERSION = 1;
    private static SQLiteDatabase database;
    private static DbController mInstance;
    private Context context;


    public DbController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getWritableDatabase();
        context = context;
    }

    public static DbController getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DbController(context);
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_VOCABULARY);
        Log.d("LuanDT", "create database");

        insertDefaultCategories(db);
        insertDefaultVocabulary(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    //insert default
    private void insertDefaultCategories(SQLiteDatabase db) {
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(ID_CATE, 1);
            values.put(ID_SERVER_CATE, 1);
            values.put(CATEGORIES_NAME, "Animals");
            values.put(CATEGORIES_STATUS, "1");
            db.insert(TABLE_CATEGORIES, null, values);

            values.put(ID_CATE, 2);
            values.put(ID_SERVER_CATE, 2);
            values.put(CATEGORIES_NAME, "Mnimals 2");
            values.put(CATEGORIES_STATUS, "1");
            db.insert(TABLE_CATEGORIES, null, values);

            values.put(ID_CATE, 3);
            values.put(ID_SERVER_CATE, 3);
            values.put(CATEGORIES_NAME, "Animals");
            values.put(CATEGORIES_STATUS, "1");
            db.insert(TABLE_CATEGORIES, null, values);

            values.put(ID_CATE, 4);
            values.put(ID_SERVER_CATE, "");
            values.put(CATEGORIES_NAME, "Mnimals 2");
            values.put(CATEGORIES_STATUS, "0");
            db.insert(TABLE_CATEGORIES, null, values);

        }
    }

    private void insertDefaultVocabulary(SQLiteDatabase db) {
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(ID_VOCA, 1);
            values.put(ID_CATE, 1);
            values.put(ENGLISH, "Dog");
            values.put(VIETNAMESE, "con chó");
            values.put(VOCABULARY_STATUS, "0");
            db.insert(TABLE_VOCABULARY, null, values);

            values.put(ID_VOCA, 2);
            values.put(ID_CATE, 1);
            values.put(ENGLISH, "hourse");
            values.put(VIETNAMESE, "con ngựa");
            values.put(VOCABULARY_STATUS, "0");
            db.insert(TABLE_VOCABULARY, null, values);

            values.put(ID_VOCA, 3);
            values.put(ID_CATE, 1);
            values.put(ENGLISH, "bird");
            values.put(VIETNAMESE, "con chim");
            values.put(VOCABULARY_STATUS, "0");
            db.insert(TABLE_VOCABULARY, null, values);

            values.put(ID_VOCA, 4);
            values.put(ID_CATE, 1);
            values.put(ID_SERVER_VOCA, 2);
            values.put(ENGLISH, "cat");
            values.put(VIETNAMESE, "con mèo");
            values.put(VOCABULARY_STATUS, "1");
            db.insert(TABLE_VOCABULARY, null, values);

            values.put(ID_VOCA, 5);
            values.put(ID_CATE, 1);
            values.put(ID_SERVER_VOCA, 5);
            values.put(ENGLISH, "duck");
            values.put(VIETNAMESE, "con vịt");
            values.put(VOCABULARY_STATUS, "1");
            db.insert(TABLE_VOCABULARY, null, values);

        }
    }

    public void openDB() throws SQLException {
        String myPath = DB_PATH + DB_NAME;

        try {
            database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor rawQuery(String sql, String[] data) {
        if (database == null)
            try {
                openDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        Cursor cursor = database.rawQuery(sql, data);
        return cursor;
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {

        if (database == null)
            try {
                openDB();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        Cursor cursor = database.query(table, columns, selection,
                selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        if (database == null)
            try {
                openDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        long insert = database.insert(table, nullColumnHack, values);
        if (insert == -1) {
            Log.d("LuanDT", "Insert error!");
        }
        return insert;
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        if (database == null)
            try {
                openDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        int ret = database.delete(table, whereClause, whereArgs);
        return ret;
    }

    //delete categories by id
    public int deleteCategoriesById(String[] id) {
        int ret = -1;
        String whereClause = ID_CATE + " IN (";
        for (int i = 0; i < id.length; i++) {
            whereClause += "?,";
        }
        whereClause = whereClause.substring(0, whereClause.length() - 1);
        whereClause += ") ";
        String[] whereArgs = id;
        ret = database.delete(TABLE_CATEGORIES, whereClause, whereArgs);
        return ret;
    }

    //update categories
    public int updateCategories(String table, ContentValues values, String whereClause, String[] whereArgs) {
        if (database == null)
            try {
                openDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        whereClause = ID_CATE + " IN (";
        whereClause += "?,";
        whereClause = whereClause.substring(0, whereClause.length() - 1);
        whereClause += ") ";

        int ret = database.update(TABLE_CATEGORIES, values, whereClause, whereArgs);
        return ret;
    }

    //delete vocabylary by id
    public int deleteVocabylaryById(String[] id) {
        int ret = -1;
        String whereClause = ID_VOCA + " IN (";
        for (int i = 0; i < id.length; i++) {
            whereClause += "?,";
        }
        whereClause = whereClause.substring(0, whereClause.length() - 1);
        whereClause += ") ";
        String[] whereArgs = id;
        ret = database.delete(TABLE_VOCABULARY, whereClause, whereArgs);
        return ret;
    }

    //delete vocabylary by id_cate
    public int deleteVocabylaryByIdCate(String[] id) {
        int ret = -1;
        String whereClause = ID_CATE + " IN (";
        for (int i = 0; i < id.length; i++) {
            whereClause += "?,";
        }
        whereClause = whereClause.substring(0, whereClause.length() - 1);
        whereClause += ") ";
        String[] whereArgs = id;
        ret = database.delete(TABLE_VOCABULARY, whereClause, whereArgs);
        return ret;
    }

    //update vocabylary
    public int updateVocabylary(String table, ContentValues values, String whereClause, String[] whereArgs) {
        if (database == null)
            try {
                openDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        whereClause = ID_VOCA + " IN (";
        whereClause += "?,";
        whereClause = whereClause.substring(0, whereClause.length() - 1);
        whereClause += ") ";

        int ret = database.update(TABLE_VOCABULARY, values, whereClause, whereArgs);
        return ret;
    }

    //delete all data
    public void deleteAllDataTable(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_CATEGORIES, null, null);
        database.delete(TABLE_VOCABULARY, null, null);

    }

}