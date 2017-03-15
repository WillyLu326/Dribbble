package willy.individual.com.dribbble.views.dribbble;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.auth.Auth;

/**
 * Created by zhenglu on 3/14/17.
 */

public class Dribbble {

    private static OkHttpClient client = new OkHttpClient();

    private static final String BASE_URL = "https://api.dribbble.com/v1/";

    private static final String SHOTS_URL = BASE_URL + "shots";

    private static final String AUTH_USER_URL = BASE_URL + "user";

    private static final String HEADER_CONTENT_TYPE = "Authorization";

    private static final String HEADER_VALUE = "Bearer " + Auth.accessToken;


    // Dribbble Functionality Method Below
    public static List<Shot> getShots(int page) {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "?page=" + page)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            Log.i("Shot Json data: ", body);
            return ModelUtils.convertToObject(body, new TypeToken<List<Shot>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User getAuthUser() {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(AUTH_USER_URL)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<User>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isLikeShot(int id) {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "/" + id + "/like")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            return body.length() != 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void likeShot(int id) {
        RequestBody requestBody = new FormBody.Builder().build();

        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "/" + id + "/like")
                .post(requestBody)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unlikeShot(int id) {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "/" + id + "/like")
                .delete()
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
