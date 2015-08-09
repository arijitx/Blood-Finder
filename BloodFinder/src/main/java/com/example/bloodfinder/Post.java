package com.example.bloodfinder;

/**
 * Created by zed_home on 7/9/15.
 */
public class Post {
    private String heading,mobile,desc,blood,datePost;
    public Post(){
        super();
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getDatePost() {

        return datePost;
    }

    public Post(String mobile, String heading, String desc, String blood, String datePost) {
        super();
        this.mobile = mobile;
        this.heading = heading;

        this.desc = desc;
        this.blood = blood;
        this.datePost=datePost;


    }

    public String getHeading() {
        return heading;
    }

    public String getMobile() {
        return mobile;
    }


    public String getDesc() {
        return desc;
    }

    public String getBlood() {
        return blood;
    }



    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }
}


