package willy.individual.com.dribbble.views.dribbble;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.views.auth.Auth;

/**
 * Created by zhenglu on 3/14/17.
 */

public class Dribbble {

    private static OkHttpClient client = new OkHttpClient();

    private static final String BASE_URL = "https://api.dribbble.com/v1/";

    public static List<Shot> getShots() {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + Auth.accessToken)
                .url(BASE_URL + "shots")
                .build();

        try {
            Response response = client.newCall(request).execute();
            response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
