package com.qianfeng.mybatis;

import com.qianfeng.oa.user.dao.IUserDAO;
import com.qianfeng.oa.user.dto.User2DTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class SpringTest {

    @Autowired
    private IUserDAO userDAO;

    @Test
    public void testCase1(){
        userDAO.queryUserById(1);
    }

    @Test
    public void testCase2(){
        userDAO.test();
    }
}
