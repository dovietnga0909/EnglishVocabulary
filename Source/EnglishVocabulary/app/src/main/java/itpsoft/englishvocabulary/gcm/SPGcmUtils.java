package itpsoft.englishvocabulary.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by LuanDT on 17/09/2015.
 */
public class SPGcmUtils {
    public static final String PREF_FILE_NAME = "gcm_pref";
    public static final String PROPERTY_REG_ID = "regId";
    public static final String PROPERTY_ON_SERVER = "onServer";

    private SharedPreferences pref;

    private SPGcmUtils(Context context) {
        this.pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SPGcmUtils utils;

    public static SPGcmUtils instance(Context context) {
        if (utils == null) {
            utils = new SPGcmUtils(context);
        }
        return utils;
    }

    public void set(String key, String value) {
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void set(String key, Boolean value) {
        Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean get(String key, Boolean defaultValue){
        boolean result;
        result = pref.getBoolean(key, defaultValue);
        return result;
    }

    public String get(String key, String defaultValue){
        String result;
        result = pref.getString(key, defaultValue);
        return result;
    }
}
