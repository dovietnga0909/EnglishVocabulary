package itpsoft.englishvocabulary.ultils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtil {
    //Ten file
    public static final String PREF_FILE_NAME = "SharedPref";
    //Ten key
    public static final String KEY_AGREE_RUN = "agree_start";
    public static final String KEY_REMIN_TIME = "remin_time";
    public static final String KEY_VOCA_DELETE = "id_voca_delete";
    public static final String KEY_VOCA_UPDATE = "id_voca_update";
    public static final String KEY_CATE_DELETE = "id_cate_delete";
    public static final String KEY_CATE_UPDATE = "id_cate_update";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_TIME_LAST_SYNC = "time_last_sync";
    public static final String KEY_SYNC = "sync";
    public static final String KEY_LOGIN = "false";
    private SharedPreferences pref;

    private SPUtil(Context context) {
        this.pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SPUtil prefUtils;

    public static SPUtil instance(Context context) {
        if (prefUtils == null) {
            prefUtils = new SPUtil(context);
        }
        return prefUtils;
    }

    public void set(String keyName, Boolean value) {
        Editor editor = pref.edit();
        editor.putBoolean(keyName, value);
        editor.commit();
    }

    public void set(String keyName, String value) {
        Editor editor = pref.edit();
        editor.putString(keyName, value);
        editor.commit();
    }

    public void set(String keyName, int value) {
        Editor editor = pref.edit();
        editor.putInt(keyName, value);
        editor.commit();
    }

    public void set(String keyName, long value) {
        Editor editor = pref.edit();
        editor.putLong(keyName, value);
        editor.commit();
    }

    public boolean get(String key, Boolean defaultValue) {
        boolean ret;
        ret = pref.getBoolean(key, defaultValue);
        return ret;
    }

    public String get(String key, String defaultValue) {
        String ret;
        ret = pref.getString(key, defaultValue);
        return ret;
    }

    public int get(String key, int defaultValue) {
        int ret;
        ret = pref.getInt(key, defaultValue);
        return ret;
    }

    public long get(String key, long defaultValue) {
        long ret;
        ret = pref.getLong(key, defaultValue);
        return ret;
    }

    public void logout(){
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
