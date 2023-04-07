package algonquin.cst2335.finalproject.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
    /**
     * 本地缓存的方法的工具类
     */
    private static final String SHARE_PREFS_NAME = "homework";

    /**
     * 存数据
     * @param ctx
     * @param key
     * @param value
     */
    public static void putString(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_MULTI_PROCESS);

        pref.edit().putString(key, value).commit();
    }

    /**
     * 获取数据
     * @param ctx
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_MULTI_PROCESS);

        return pref.getString(key, defaultValue);
    }



}