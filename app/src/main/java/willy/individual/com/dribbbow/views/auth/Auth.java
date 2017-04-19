package willy.individual.com.dribbbow.views.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import willy.individual.com.dribbbow.models.User;
import willy.individual.com.dribbbow.utils.ModelUtils;

public class Auth {

    public static final String AUTH_GET_URL = "https://dribbble.com/oauth/authorize";

    public static final String AUTH_POST_URL = "https://dribbble.com/oauth/token";

    public static final String REDIRECT_URL = "http://www.zhenglu326.com";

    public static final String CLIENT_ID = "76048d257d97a98958efb5bdf0ccbc521b793af5725dbab3b67c55a672080bf4";

    public static final String SCOPE = "public+write";

    public static final String STATE = "willylu_secret";

    public static final String CLIENT_SECRET = "8f66398d59f0e67a5b66df946777d1c92e7dc3d7e43e7235562a6c966202b9cf";

    public static final String ACCESS_TOKEN_SP_KEY = "access token sp key";

    public static final String AUTH_TOKEN_SP = "auth_token";

    public static final String AUTH_USER_SP = "auth_user";

    public static final String ACCESS_USER_SP_KEY = "access user sp key";

    public static final String CLIENT_ACCESS_TOKEN = "5479178e668d79cd23fab535529ca122a1c292507acc127d40c0ae87feb5c022";

    public static String accessToken = CLIENT_ACCESS_TOKEN;

    public static User authUser;


    // Auth Functions
    public static String doGetRequestUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(AUTH_GET_URL)
                .append("?client_id=")
                .append(CLIENT_ID)
                .append("&redirect_uri=")
                .append(REDIRECT_URL)
                .append("&scope=")
                .append(SCOPE)
                .append("&state=")
                .append(STATE);
        return sb.toString();
    }

    public static String fetchAccessToken(String code) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("code", code)
                .add("redirect_uri", REDIRECT_URL)
                .build();
        Request request = new Request.Builder()
                .url(AUTH_POST_URL)
                .post(body)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.get("access_token").toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return "error";
        }

    }

    public static void init(Context context) {
        accessToken = loadAccessToken(context);
        if (accessToken != CLIENT_ACCESS_TOKEN) {
            authUser = loadAuthUser(context);
        }
    }

    public static void login(Context context, String accessToken) {
        Auth.accessToken = accessToken;
        saveAccessToken(context, accessToken);
    }

    public static boolean isLogin() {
        return accessToken != CLIENT_ACCESS_TOKEN;
    }

    public static void saveAccessToken(Context context, String accessToken) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_TOKEN_SP, context.MODE_PRIVATE);
        sp.edit().putString(ACCESS_TOKEN_SP_KEY, accessToken).apply();
    }

    public static String loadAccessToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_TOKEN_SP, context.MODE_PRIVATE);
        return sp.getString(ACCESS_TOKEN_SP_KEY, null);
    }

    public static void clearAccessToken(Context context) {
        accessToken = CLIENT_ACCESS_TOKEN;
        SharedPreferences sp = context.getSharedPreferences(AUTH_TOKEN_SP, context.MODE_PRIVATE);
        sp.edit().putString(ACCESS_TOKEN_SP_KEY, null).apply();
    }

    public static void saveAuthUser(Context context, User user) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_USER_SP, context.MODE_PRIVATE);
        sp.edit().putString(ACCESS_USER_SP_KEY, ModelUtils.convertToString(user, new TypeToken<User>(){})).apply();
    }

    //
    public static User loadAuthUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_USER_SP, context.MODE_PRIVATE);
        return ModelUtils.convertToObject(sp.getString(ACCESS_USER_SP_KEY, ""), new TypeToken<User>(){});
    }
}
