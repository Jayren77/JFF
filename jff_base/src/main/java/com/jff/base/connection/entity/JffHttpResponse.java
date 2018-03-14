package com.jff.base.connection.entity;


import com.jff.base.entity.IJffResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * http(s)请求结果封装类
 */
@Getter
@Setter
@ToString
public class JffHttpResponse implements IJffResponse{
    private String msg;
    private int statusCode;
}
