package com.jff.base.usr.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 基础响应类
 */
@Getter
@Setter
@ToString
public class BaseResponse<T> {


    private String msg;

    /**
     * 请求是否成功
     */
    private boolean flag;

    /**
     * 结果类
     */
    private T t;

    /**
     * 结果类集合
     */
    private List<T> ts;

}
