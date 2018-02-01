package com.jff.base.usr.entity;

import java.util.List;

/**
 * 基础响应类
 */
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public List<T> getTs() {
        return ts;
    }

    public void setTs(List<T> ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BaseResponse{");
        sb.append("msg='").append(msg).append('\'');
        sb.append(", flag=").append(flag);
        sb.append(", t=").append(t);
        sb.append(", ts=").append(ts);
        sb.append('}');
        return sb.toString();
    }
}
