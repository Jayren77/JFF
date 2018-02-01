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
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/wx/login",method = RequestMethod.POST)
    public BaseResponse wxLogin(@RequestBody UserEntity userEntity){
        LOGGER.info(UserUtils.toJson(userEntity));
        return null;
    }
}
