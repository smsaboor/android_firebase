package com.example.flutter_projects;

public class ContactModel {
    ContactModel(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ContactModel(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    String name;
    String mobile;

}
