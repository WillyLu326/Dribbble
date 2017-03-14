package willy.individual.com.dribbble.views.dribbble;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.auth.Auth;

/**
 * Created by zhenglu on 3/14/17.
 */

public class Dribbble {

    private static OkHttpClient client = new OkHttpClient();

    private static final String BASE_URL = "https://api.dribbble.com/v1/";

    private static final String SHOTS_URL = BASE_URL + "shots";

    public static List<Shot> getShots(int page) {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + Auth.accessToken)
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
}
