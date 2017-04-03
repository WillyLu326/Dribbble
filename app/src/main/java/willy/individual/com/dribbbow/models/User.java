package willy.individual.com.dribbbow.models;

import java.util.Map;

/**
 * Created by zhenglu on 3/14/17.
 */

public class User {

    public int id;
    public String name;
    public String username;
    public String html_url;
    public String avatar_url;
    public String bio;
    public String location;
    public Map<String, String> links;
    public User followee;
    public User follower;
    public int buckets_count;
    public int comments_received_count;
    public int followers_count;
    public int followings_count;
    public int likes_count;
    public int likes_received_count;
    public int projects_count;
    public int rebounds_received_count;
    public int shots_count;
    public int teams_count;
    public boolean can_upload_shot;
    public String type;
    public boolean pro;
    public String buckets_url;
    public String followers_url;
    public String following_url;
    public String likes_url;
    public String shots_url;
    public String teams_url;
    public String created_at;
    public String updated_at;

    public boolean isFollowing;
}