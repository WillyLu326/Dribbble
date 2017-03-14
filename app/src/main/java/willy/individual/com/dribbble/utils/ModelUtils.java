package willy.individual.com.dribbble.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by zhenglu on 3/9/17.
 */

public class ModelUtils {

    private static Gson gson = new Gson();

    private static final String SP_KEY = "model";

    public static void save(Context context, String key, Object model) {
        SharedPreferences sp = context.getSharedPreferences(SP_KEY, context.MODE_PRIVATE);
        sp.edit().putString(key, gson.toJson(model)).apply();
    }

    public static <T> T read(Context context, String key, TypeToken<T> typeToken) {
        SharedPreferences sp = context.getSharedPreferences(SP_KEY, context.MODE_PRIVATE);
        String value = sp.getString(key, null);
        return gson.fromJson(value, typeToken.getType());
    }

    public static <T> String convertToString(T object, TypeToken<T> typeToken) {
        return gson.toJson(object, typeToken.getRawType());
    }

    public static <T> T convertToObject(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }
}
