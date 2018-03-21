package com.qianfeng.oa.user.dao;

import com.qianfeng.oa.user.dto.User2DTO;

public interface IUserDAO {

    User2DTO queryUserById(Integer id);

    void saveUser();

    void query();
    void test();
}
