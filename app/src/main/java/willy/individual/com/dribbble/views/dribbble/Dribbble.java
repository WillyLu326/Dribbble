package willy.individual.com.dribbble.views.dribbble;

import android.support.design.widget.Snackbar;

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
import willy.individual.com.dribbble.views.base.DribbbleException;


public class Dribbble {

    private static OkHttpClient client = new OkHttpClient();

    private static final String BASE_URL = "https://api.dribbble.com/v1/";

    private static final String SHOTS_URL = BASE_URL + "shots";

    private static final String AUTH_USER_URL = BASE_URL + "user";

    public static final String BUCKET_AUTH_USER_URL = "https://api.dribbble.com/v1/user/buckets";

    private static final String HEADER_CONTENT_TYPE = "Authorization";

    private static final String HEADER_VALUE = "Bearer " + Auth.accessToken;


    // Dribbble Functionality Method Below
    public static List<Shot> getPopularShots(int page) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "?page=" + page)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            return ModelUtils.convertToObject(body, new TypeToken<List<Shot>>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<Shot> getLikeShots(int page) throws DribbbleException {
        List<Shot> likeShots = new ArrayList<>();
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "users/" + getAuthUser().id + "/likes?page=" + page)
                .build();
        try {
            Response response = client.newCall(request).execute();
            List<Like> likes = ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Like>>(){});
            for (Like like : likes) {
                likeShots.add(like.shot);
            }
            return likeShots;
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static User getAuthUser() throws DribbbleException{
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(AUTH_USER_URL)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<User>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static boolean isLikeShot(int id) throws DribbbleException{
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "/" + id + "/like")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            return body.length() != 0;
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static void likeShot(int id) throws DribbbleException{
        RequestBody requestBody = new FormBody.Builder().build();

        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "/" + id + "/like")
                .post(requestBody)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static void unlikeShot(int id) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(SHOTS_URL + "/" + id + "/like")
                .delete()
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<Comment> getComments(String url, int page) throws DribbbleException{
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(url + "?page=" + page)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Comment>>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<Bucket> getBuckets(int page) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BUCKET_AUTH_USER_URL + "?page=" + page)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Bucket>>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<Bucket> getShotBuckets(String url, int page) throws DribbbleException{

        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(url + "?page=" + page)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Bucket>>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<Shot> getBucketShots(int id, int page) throws DribbbleException {
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

    public static Bucket postNewBucket(String bucketName, String bucketDescription) throws DribbbleException {
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
            throw new DribbbleException(e.getMessage());
        }
    }

    public static Bucket putExistBucket(int bucketId, String bucketName, String bucketDescription) throws DribbbleException {
        RequestBody body = new FormBody.Builder()
                .add("name", bucketName)
                .add("description", bucketDescription)
                .build();

        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "buckets/" + bucketId)
                .put(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<Bucket>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static void deleteExistBucket(int bucketId) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "/buckets/" + bucketId)
                .delete()
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static void updateShotBucket(int bucketId, int shotId) throws DribbbleException {
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
            throw new DribbbleException(e.getMessage());
        }
    }

    public static void deleteShotBucket(int bucketId, int shotId) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "/buckets/" + bucketId + "/shots?shot_id=" + shotId)
                .delete()
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<Bucket> getAllBuckets(String url) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(url + "?per_page=" + Integer.MAX_VALUE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Bucket>>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<Bucket> getAllUserBuckets() throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "user/buckets?per_page=" + Integer.MAX_VALUE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Bucket>>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<User> getFollowingUsers(String url, int page) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(url + "?page=" + page)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<User>>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<User> getFollowerUser(String url, int page) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(url + "?page=" + page)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<User>>(){});
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static List<Shot> getSpecificUserShots(String username, int page) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "users/" + username + "/shots?page=" + page)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return ModelUtils.convertToObject(response.body().string(), new TypeToken<List<Shot>>(){});
        } catch (IOException e) {
           throw new DribbbleException(e.getMessage());
        }
    }

    public static void followUser(String username) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "/users/" + username + "/follow")
                .put(new FormBody.Builder().build())
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static void unfollowUser(String username) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "/users/" + username + "/follow")
                .delete()
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    public static boolean isFollowingUser(String username) throws DribbbleException {
        Request request = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE)
                .url(BASE_URL + "/user/following/" + username)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.code() == 204;
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }
}
