package willy.individual.com.dribbble.models;

import java.util.List;
import java.util.Map;


public class Shot {

    public int id;
    public String title;
    public String description;
    public int width;
    public int height;
    public Map<String, String> images;
    public int views_count;
    public int likes_count;
    public int comments_count;
    public int attachments_count;
    public int rebounds_count;
    public int buckets_count;
    public String created_at;
    public String updated_at;
    public String html_url;
    public String attachments_url;
    public String buckets_url;
    public String comments_url;
    public String likes_url;
    public String projects_url;
    public String rebounds_url;
    public boolean animated;
    public List<String> tags;
    public User user;


    // customized property
    public boolean isLike;

    public String getImageUrl() {
        return images.containsKey("hidpi") ? images.get("hidpi") : images.get("normal");
    }
}

