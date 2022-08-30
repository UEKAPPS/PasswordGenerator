package com.uekapps.passwordgenerator;

public class Model {
    String id, title, password;

    public Model(String id, String title, String password) {
        this.id = id; // 0
        this.title = title; // 1
        this.password = password; // 2
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
