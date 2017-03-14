package willy.individual.com.dribbble.models;

import java.util.Map;

/**
 * Created by zhenglu on 3/5/17.
 */

public class Shot {

    public int views_count;
    public int likes_count;
    public int butckets_count;

    public Map<String, String> images;

    public String getImageUrl() {
        return images.containsKey("hidpi") ? images.get("hidpi") : images.get("normal");
    }
}
