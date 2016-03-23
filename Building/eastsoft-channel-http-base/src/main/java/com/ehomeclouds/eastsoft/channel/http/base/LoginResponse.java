package com.ehomeclouds.eastsoft.channel.http.base;

import org.apache.http.HttpResponse;

/**
 * Created by sun on 2015/6/4 0004.
 */
public class LoginResponse  {
    private String id;
    private String email;
    private String name;
    private String rolecode;
    private String userName;
    private String domain;
    private String brokerName;
    private String brokerPassword;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerPassword() {
        return brokerPassword;
    }

    public void setBrokerPassword(String brokerPassword) {
        this.brokerPassword = brokerPassword;
    }

    public String getRolecode() {
        return rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return rolecode;
    }

    public void setRole(String role) {
        this.rolecode = role;
    }

    @Override
    public String toString() {
        return "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
