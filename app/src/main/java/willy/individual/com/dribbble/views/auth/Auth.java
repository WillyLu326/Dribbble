package willy.individual.com.dribbble.views.auth;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth {

    public static final String AUTH_GET_URL = "https://dribbble.com/oauth/authorize";

    public static final String AUTH_POST_URL = "https://dribbble.com/oauth/token";

    public static final String REDIRECT_URL = "http://www.zhenglu326.com";

    public static final String CLIENT_ID = "76048d257d97a98958efb5bdf0ccbc521b793af5725dbab3b67c55a672080bf4";

    public static final String SCOPE = "public+write";

    public static final String STATE = "willylu_secret";

    public static final String CLIENT_SECRET = "8f66398d59f0e67a5b66df946777d1c92e7dc3d7e43e7235562a6c966202b9cf";


    public static String getDribbbleGetRequestUrl() {
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
}
