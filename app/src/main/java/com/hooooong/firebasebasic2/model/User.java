package com.hooooong.firebasebasic2.model;

/**
 * Created by Android Hong on 2017-10-31.
 */

public class User {
    private String id;
    private String token;

    public User() {
        // default for firebase realtime database;
    }

    public User(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
