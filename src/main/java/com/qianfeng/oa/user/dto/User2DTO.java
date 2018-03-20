package com.qianfeng.oa.user.dto;

public class User2DTO {

    private Integer userI;
    private String username;
    private String email;
    private String password;
    private Character sex;
    private DepartmentDTO departmentDTO;

    public DepartmentDTO getDepartmentDTO() {
        return departmentDTO;
    }

    public void setDepartmentDTO(DepartmentDTO departmentDTO) {
        this.departmentDTO = departmentDTO;
    }

    public Integer getUserI() {
        return userI;
    }

    public void setUserI(Integer userI) {
        this.userI = userI;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }
}
