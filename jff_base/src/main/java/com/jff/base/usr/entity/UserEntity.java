package com.jff.base.usr.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * 用户表
 */
public class UserEntity implements UserDetails{

    private int id;
    private String userName;
    private String userPassword;
    private String gender;
    private int userRole;
    private String userLocation;
    private Timestamp userCreTime;
    private String userWxName;
    private String userHeader;
    private int tryCount;
    private int loginCount;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
        if(null != userName){
            this.userName = userName;
        }
        this.userName = this.userWxName;

    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
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

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserEntity{");
        sb.append("id=").append(id);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userPassword='").append(userPassword).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", userRole=").append(userRole);
        sb.append(", userLocation='").append(userLocation).append('\'');
        sb.append(", userCreTime=").append(userCreTime);
        sb.append(", userWxName='").append(userWxName).append('\'');
        sb.append(", userHeader='").append(userHeader).append('\'');
        sb.append(", tryCount=").append(tryCount);
        sb.append(", loginCount=").append(loginCount);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return getUserPassword();
    }

    @Override
    public String getUsername() {
        return getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * 账户是否被锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        if(this.tryCount>3){
            return true;
        }
        return false;
    }

    /**
     * 认证是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 是不是激活的
     * @return
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}
