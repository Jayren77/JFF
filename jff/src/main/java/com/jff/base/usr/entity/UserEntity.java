package com.jff.base.usr.entity;

import java.sql.Timestamp;

/**
 * 用户表
 */
public class UserEntity {

    private int id;
    private String userName;
    private String userPassword;
    private int userRole;
    private String userLocation;
    private Timestamp userCreTime;
    private String userWxName;
    private String userHeader;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public Timestamp getUserCreTime() {
        return userCreTime;
    }

    public void setUserCreTime(Timestamp userCreTime) {
        this.userCreTime = userCreTime;
    }

    public String getUserWxName() {
        return userWxName;
    }

    public void setUserWxName(String userWxName) {
        this.userWxName = userWxName;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserEntity{");
        sb.append("id=").append(id);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userPassword='").append(userPassword).append('\'');
        sb.append(", userRole='").append(userRole).append('\'');
        sb.append(", userLocation='").append(userLocation).append('\'');
        sb.append(", userCreTime=").append(userCreTime);
        sb.append(", userWxName='").append(userWxName).append('\'');
        sb.append(", userHeader='").append(userHeader).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
