package com.ehomeclouds.eastsoft.channel.http.base;

/**
 * Created by ll on 2016/3/31.
 */
public class LoginRequest {
    public String username;
    public String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String username) {
        this.username = username;
    }
}
