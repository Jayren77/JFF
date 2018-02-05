package com.jff.base.usr.dao;

import com.jff.base.usr.entity.WxLoginInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 存储查询wx小程序的登录信息
 *
 * 记录：由于这种内容必然会需要多次提取，所以应该存入缓存
 *
 */
@Mapper
@Component
@CacheConfig(cacheNames = "wxlogininfo")
public interface WxLoginInfoMapper {

    @Insert("insert into wx_login_info (app_id,secret,js_code,grant_type) values (#{appId},#{secret},#{jsCode},#{grantType});")
    public int insert(WxLoginInfo info);

    @Cacheable
    @Select("select * from wx_login_info")
    public WxLoginInfo select();
}
