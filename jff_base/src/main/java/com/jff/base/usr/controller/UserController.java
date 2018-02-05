package com.jff.base.usr.controller;

import com.jff.base.usr.entity.BaseResponse;
import com.jff.base.usr.entity.UserEntity;
import com.jff.base.usr.util.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

/**
 * 用户验证
 */
@RestController
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    /**
     * 入库微信端的用户信息
     * 通过微信登录的用户，用户信息的验证部分其实是交给微信去做的，后台只需要验证登录信息以及记录登录信息即可。
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/wx/login",method = RequestMethod.POST)
    public BaseResponse wxLogin(@RequestBody UserEntity userEntity){
        LOGGER.info(UserUtils.toJson(userEntity));
        return null;
    }
}
