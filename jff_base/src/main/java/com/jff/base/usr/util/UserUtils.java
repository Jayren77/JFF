package com.jff.base.usr.util;

import com.google.gson.Gson;

/**
 * 工具类
 */
public class UserUtils {

    private UserUtils(){}

    private static final Gson gson = new Gson();

    /**
     * 同gson.toJson();
     * @param o
     * @return
     */
    public static String toJson(Object o){
        return gson.toJson(o);
    }
}
