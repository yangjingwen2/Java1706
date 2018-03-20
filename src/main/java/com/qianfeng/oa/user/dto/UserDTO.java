package com.qianfeng.oa.user.dto;

public class UserDTO {

    private Integer user_id;
    private String user_name;
    private String user_email;
    private String user_password;
    private Character user_sex;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public Character getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(Character user_sex) {
        this.user_sex = user_sex;
    }
}
