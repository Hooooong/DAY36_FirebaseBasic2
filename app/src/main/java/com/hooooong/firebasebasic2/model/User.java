package com.hooooong.firebasebasic2.model;

import java.io.Serializable;

/**
 * Created by Android Hong on 2017-10-31.
 */

public class User implements Serializable{
    private String id;
    private String token;

    private String email;

    public User() {
        // default for firebase realtime database;
    }

    public User(String id, String token, String email) {
        this.id = id;
        this.token = token;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
