package com.jff.base.usr.dao;

import com.jff.base.usr.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    @Select("select * from user_info where user_name = #{name}")
    public UserEntity queryUserByName(@Param("name") String userName);

    @Select("select * from user_info")
    public List<UserEntity> queryAll();

    @Insert("insert into user_info (user_name,user_password,user_role,user_cre_time,user_location,user_wx_name,user_header) " +
            "values (#{userName},#{userPassword},#{userRole},#{userCreTime},#{userLocation},#{userWxName},#{userHeader})")
    public int insert(UserEntity userEntity);

    @Update("update user_info set user_name = #{newName} where user_name = #{oldName} ")
    public int updateName(@Param("newName") String newName,@Param("oldName")String oldName);

    @Update("update user_info set user_location = #{newLocation} where user_name = #{userName} ")
    public int updateLocation(@Param("newLocation") String newLocation,@Param("userName")String userName);

    @Update("update user_info set user_password = #{password} where user_name = #{userName} ")
    public int updatePassword(@Param("password") String password,@Param("userName")String userName);

    @Update("update user_info set user_header = #{img} where user_name = #{userName} ")
    public int updateHeader(@Param("img") String img,@Param("userName")String userName);

    @Delete("delete from user_info where user_name = #{userName} ")
    public int delete(@Param("userName")String userName);

}
