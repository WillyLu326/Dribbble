package willy.individual.com.dribbble.views.dribbble;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import willy.individual.com.dribbble.models.Bucket;
import willy.individual.com.dribbble.models.Comment;
import willy.individual.com.dribbble.models.Like;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.auth.Auth;


public class Dribbble {

    private static OkHttpClient client = new OkHttpClient();

    private static final String BASE_URL = "https://api.dribbble.com/v1/";

    private static final String SHOTS_URL = BASE_URL + "shots";

    private static final String AUTH_USER_URL = BASE_URL + "user";

    public static final String BUCKET_AUTH_USER_URL = "https://api.dribbble.com/v1/user/buckets";

    /**
     * BASE_URL + "/buckets/" + bucket_id + "/shots"
     */
    private static final String SHOTS_OF_BUCKET = "https://api.dribbble.com/v1/buckets/491275/shots";


    private static final String HEADER_CONTENT_TYPE = "Authorization";

    private static final String HEADER_VALUE = "Bearer " + Auth.accessToken;

    private static final int AUTH_USER_ID = getAuthUser().id;

    // Dribbble Functionality Method Below
    public static List<Shot> getPopularShots(int page) {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "?page=" + page)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            return ModelUtils.convertToObject(body, new TypeToken<List<Shot>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Shot> getLikeShots(int page) {
        List<Shot> likeShots = new ArrayList<>();
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "users/" + AUTH_USER_ID + "/likes?page=" + page)
                .build();
        try {
            Response response = client.newCall(request).execute();
            //System.out.println(response.body().string());
            List<Like> likes = ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Like>>(){});
            for (Like like : likes) {
                likeShots.add(like.shot);
            }
            System.out.println("====================");
            System.out.println("========= " + likeShots.size() + " ===========");
            System.out.println("====================");

            return likeShots;
        } catch (IOException e) {
            e.printStackTrace();
            return likeShots;
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

    public static List<Comment> getComments(String url, int page) {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(url + "?page=" + page)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Comment>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Bucket> getBuckets(int page) {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BUCKET_AUTH_USER_URL + "?page=" + page)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Bucket>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Bucket> getShotBuckets(String url, int page) {

        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(url + "?page=" + page)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();

            return ModelUtils.convertToObject(body, new TypeToken<List<Bucket>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Shot> getBucketShots(int id, int page) {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url("https://api.dribbble.com/v1/buckets/" + id + "/shots?page=" + page)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Shot>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bucket postNewBucket(String bucketName, String bucketDescription) {
        RequestBody body = new FormBody.Builder()
                .add("name", bucketName)
                .add("description", bucketDescription)
                .build();

        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "buckets")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<Bucket>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateBucketShot(int bucketId, int shotId) {
        RequestBody body = new FormBody.Builder()
                .add("shot_id", shotId + "")
                .build();

        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "/buckets/" + bucketId + "/shots")
                .put(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Bucket> getAllBuckets(String url) {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(url + "?per_page=" + Integer.MAX_VALUE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Bucket>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Bucket> getAllUserBuckets() {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "user/buckets?per_page=" + Integer.MAX_VALUE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Bucket>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
