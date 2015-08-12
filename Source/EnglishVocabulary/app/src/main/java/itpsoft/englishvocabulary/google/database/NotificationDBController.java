package itpsoft.englishvocabulary.google.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import itpsoft.englishvocabulary.google.model.Notification;


public class NotificationDBController extends SQLiteOpenHelper {

	public static String NOTIFICATION_STATE_NEW = "new";
	public static String NOTIFICATION_STATE_CHECKED = "checked";

	private static String DB_NAME = "notification.db";
	private static int DB_VERSION = 1;
	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/itp.android.gcm/databases/";  // db path

	private SQLiteDatabase mDatabase;
	private static NotificationDBController sInstance;
	public NotificationDBController(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mDatabase = getWritableDatabase();
	}

	public static NotificationDBController getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new NotificationDBController(context);
		}

		return sInstance;
	}

	public static String TABLE_NAME = "notification_tbl";
	public static String ID_COL = "_id";
	public static String TITLE_COL = "title";
	public static String CONTENT_COL = "content";
	public static String URL_IMAGE_COL = "image_url";
	public static String URL_COL = "url";
	public static String DATE_COL = "date";
	public static String TIME_COL = "time";
	public static String STATE_COL = "state";

	private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "(" + ID_COL + " integer primary key autoincrement,"
			+ TITLE_COL + " text," + CONTENT_COL + " text,"
			+ URL_IMAGE_COL + " text," + URL_COL + " text,"
			+ DATE_COL + " text," + TIME_COL + " text,"
			+ STATE_COL + " text);";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public void openDB() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		try {
			mDatabase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (Exception e) {
			
		}
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		if (mDatabase == null)
			openDB();

		Cursor cursor = mDatabase.query(table, columns, selection,
				selectionArgs, groupBy, having, orderBy);
		return cursor;
	}

	public Cursor rawQuery(String stSql, String[] data) {
		if (mDatabase == null)
			openDB();

		Cursor cursor = mDatabase.rawQuery(stSql, data);
		return cursor;
	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		if (mDatabase == null)
			openDB();
		long insertRet = 0;
		try {
			insertRet = mDatabase.insert(table, nullColumnHack, values);
		} catch (Exception e) {
			
		}

		return insertRet;
	}
	
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		if (mDatabase == null)
			openDB();

		int ret = mDatabase.update(table, values, whereClause, whereArgs);

		return ret;
	}

	public void checkedNotification(Notification item, int id) {
		ContentValues values = new ContentValues();
		values.put(STATE_COL, NOTIFICATION_STATE_CHECKED);

		String where = NotificationDBController.ID_COL + " = " + id;
		update(TABLE_NAME, values, where, null);
	}

	public void deleteAllData() {
		SQLiteDatabase sdb = this.getWritableDatabase();
		sdb.delete(TABLE_NAME, null, null);

	}

	public void getSize() {
	}
}