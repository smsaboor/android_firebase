package com.example.flutter_projects.firebase;

import java.util.List;

public class User {
    private String name;
    private String email;
    private String mobile;
    private String uid;

    public User() {}

    public User(String name, String email, String mobile, String uid) {
        // ...
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}