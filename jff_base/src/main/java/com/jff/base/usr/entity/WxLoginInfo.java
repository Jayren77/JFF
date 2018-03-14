package com.jff.base.usr.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用来维护微信登录验证所需的内容
 * https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
 */
@Getter
@Setter
@ToString
public class WxLoginInfo {

    /**
     * 小程序唯一标识
     */
    private String appId;

    /**
     * 小程序的 app secret
     */
    private String secret;

    /**
     * 登录时获取的 code
     */
    private String jsCode;

    /**
     * 填写为 authorization_code
     */
    private String grantType;


}
