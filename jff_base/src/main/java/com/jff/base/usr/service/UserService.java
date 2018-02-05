package com.jff.base.usr.service;

import com.jff.base.usr.dao.UserMapper;
import com.jff.base.usr.entity.UserEntity;
import com.jff.base.usr.entity.WxResponse;
import com.jff.base.usr.util.UserConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService{

    private static final Logger LOG = Logger.getLogger(UserService.class);
    @Autowired
    private UserMapper userMapper;

    /**
     * 请求微信https接口，用code换  openid session_key unionid;
     * @return 封装后wx返回消息
     */
    public WxResponse checkWxLoginCode(){
        //how 开启HTTPS请求

        return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       LOG.info("验证："+username);
       UserEntity user = userMapper.queryUserByName(username);
       if(null == user){
           throw new UsernameNotFoundException(UserConstant.NOT_FOUND_USER);
       }
       return user;
    }
}
