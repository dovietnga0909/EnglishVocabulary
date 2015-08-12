package itpsoft.englishvocabulary.google.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Utils {
	static final String MY_SHARED_PREFERENCE = "MySharedPreference";
	
	
	public static void saveBoolean(Context context, String keyName,
			Boolean value) {
		SharedPreferences pref = context.getSharedPreferences(
				MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

		Editor editor = pref.edit();
		editor.putBoolean(keyName, value);

		editor.commit();
	}

	public static boolean getBoolean(Context context, String key,
			Boolean defaultValue) {
		boolean ret = false;
		try {
			SharedPreferences pref = context.getSharedPreferences(
					MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
			ret = pref.getBoolean(key, defaultValue);
		} catch (Exception e) {

		}
		return ret;
	}

	public static void saveString(Context context, String key, String value) {
		SharedPreferences pref = context.getSharedPreferences(
				MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(Context context, String key, String defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(
				MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

		return pref.getString(key, defaultValue);
	}

	public static void saveInt(Context context, String key, int value) {
		SharedPreferences pref = context.getSharedPreferences(
				MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void saveLong(Context context, String key, long value) {
		SharedPreferences pref = context.getSharedPreferences(
				MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

		Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static int getInt(Context context, String key) {
		int ret = 0;
		try {
			SharedPreferences pref = context.getSharedPreferences(
					MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

			ret = pref.getInt(key, 0);
		} catch (Exception e) {

		}
		return ret;
	}

	public static long getLong(Context context, String key) {
		long ret = 0;
		try {
			SharedPreferences pref = context.getSharedPreferences(
					MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);

			ret = pref.getLong(key, 0);
		} catch (Exception e) {

		}
		return ret;
	}
}
