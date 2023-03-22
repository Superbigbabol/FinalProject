package algonquin.cst2335.finalproject.untils;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class PrefUtils {

    private static final String SHARE_PREFS_NAME = "homework";


    public static void putString(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_MULTI_PROCESS);

        pref.edit().putString(key, value).commit();
    }


    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_MULTI_PROCESS);

        return pref.getString(key, defaultValue);
    }



}