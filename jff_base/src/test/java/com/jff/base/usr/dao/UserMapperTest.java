package com.jff.base.usr.dao;

import com.jff.base.JffBaseApplication;
import com.jff.base.usr.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JffBaseApplication.class)
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    private final static String TEST_STR = "test";
	@Test
	public void TestInsert() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(TEST_STR);
        userEntity.setUserHeader(TEST_STR);
        userEntity.setUserLocation(TEST_STR);
        userEntity.setUserPassword(TEST_STR);
        userEntity.setUserRole(1);
        userMapper.insert(userEntity);
	}

}
