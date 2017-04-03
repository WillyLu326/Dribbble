package willy.individual.com.dribbbow.models;

import java.util.Date;


public class Comment {

    public int id;
    public String body;
    public int likes_count;
    public String likes_url;
    public Date created_at;
    public Date updated_at;
    public User user;

}
