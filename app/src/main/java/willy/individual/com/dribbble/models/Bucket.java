package willy.individual.com.dribbble.models;

import java.util.Date;


public class Bucket {

    public int id;
    public String name;
    public String description;
    public int shots_count;
    public Date created_at;
    public Date updated_at;
    public User user;
    public boolean isChoosing;

}
