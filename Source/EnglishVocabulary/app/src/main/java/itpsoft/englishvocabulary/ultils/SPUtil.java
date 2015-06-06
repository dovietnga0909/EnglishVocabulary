package itpsoft.englishvocabulary.ultils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtil {
    //Ten file
    public static final String PREF_FILE_NAME = "SharedPref";
    //Ten key
    public static final String KEY_AGREE_RUN = "agree_start";
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

    public boolean get(String key, Boolean defaultValue) {
        boolean ret;
        ret = pref.getBoolean(key, defaultValue);
        return ret;
    }

    public int get(String key, int defaultValue) {
        int ret;
        ret = pref.getInt(key, defaultValue);
        return ret;
    }
}
