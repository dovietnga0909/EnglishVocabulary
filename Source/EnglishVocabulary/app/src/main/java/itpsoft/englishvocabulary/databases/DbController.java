package itpsoft.englishvocabulary.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luand_000 on 03/06/2015.
 */
public class DbController extends SQLiteOpenHelper {

    private static String DB_NAME = "english_vocabulary";
    private static String DB_PATH = "/data/data/itpsoft.englishvocabulary/databases/";
    private static int DB_VERSION = 1;

    private static SQLiteDatabase database;
    private Context context;

    //Table category


    public DbController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getWritableDatabase();
        context = context;
    }

    private static DbController mInstance;
    public static DbController getInstance(Context context){
        if(mInstance == null)
            mInstance = new DbController(context);
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
