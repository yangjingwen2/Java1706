package com.qianfeng.oa.user.dao.impl;

import com.qianfeng.oa.user.dao.IUserDAO;
import com.qianfeng.oa.user.dto.User2DTO;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserDAO extends SqlSessionDaoSupport implements IUserDAO {

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }


    public User2DTO queryUserById(Integer id) {
        List<Object> list = getSqlSession().selectList("com.qianfeng.oa.user.dto.UserMapper.queryUser");
        System.out.println(list.size());
        return null;
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void saveUser() {
//        User2DTO user2DTO = new User2DTO();
//        user2DTO.setUsername("找刘");
//        user2DTO.setEmail("xxxx");
//        user2DTO.setPassword("11111");
//        user2DTO.setSex('d');
//        getSqlSession().insert("com.qianfeng.oa.user.dto.UserMapper.save",user2DTO);

//        new Thread(new Runnable() {
//            public void run() {
//                System.out.println("------------");
//                getSqlSession().selectOne("com.qianfeng.oa.user.dto.UserMapper.queryUserById",6);
//                System.out.println(">>>>>>>>>>>>>>>>");
//            }
//        }).start();


        getSqlSession().selectOne("com.qianfeng.oa.user.dto.UserMapper.queryUserById",6);



    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void query() {
        getSqlSession().selectOne("com.qianfeng.oa.user.dto.UserMapper.queryUserById",6);
    }

    public void test() {
        saveUser();
        query();
    }


}
