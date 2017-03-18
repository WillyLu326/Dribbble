package willy.individual.com.dribbble.models;

import java.util.Date;

/**
 * Created by zhenglu on 3/18/17.
 */

public class Comment {

    public int id;
    public String body;
    public int likes_count;
    public String likes_url;
    public Date created_at;
    public Date updated_at;
    public User user;

    public Comment(String body) {
        this.body = body;
    }
}
