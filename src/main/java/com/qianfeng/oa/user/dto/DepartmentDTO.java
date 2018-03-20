package com.qianfeng.oa.user.dto;

import java.util.List;

public class DepartmentDTO {

    private Integer id;
    private String name;
    private List<User2DTO> users;

    public List<User2DTO> getUsers() {
        return users;
    }

    public void setUsers(List<User2DTO> users) {
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
