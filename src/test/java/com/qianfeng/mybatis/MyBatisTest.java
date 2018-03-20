package com.qianfeng.mybatis;

import com.qianfeng.oa.user.dto.DepartmentDTO;
import com.qianfeng.oa.user.dto.User2DTO;
import com.qianfeng.oa.user.dto.UserDTO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * session需要在使用完毕之后关闭连接（释放连接）
 */
public class MyBatisTest {
    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void init(){
        //加载mybatis的核心配置文件
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream("mybatis.cfg.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //解析mybatis.cfg.xml文档，并且初始化MyBatis（连接池、缓存）
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 查询所有
     */
    @Test
    public void testCase1(){
        try {
            //加载mybatis的核心配置文件
            InputStream inputStream = Resources.getResourceAsStream("mybatis.cfg.xml");
            //解析mybatis.cfg.xml文档，并且初始化MyBatis（连接池、缓存）
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            //获取session
            SqlSession sqlSession = sqlSessionFactory.openSession();
            //增删改查
            List<UserDTO> list = sqlSession.selectList("com.qianfeng.oa.user.dto.UserMapper.queryUser");
            for (UserDTO user:list) {
                System.out.println(user.getUser_name());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCase2(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //参数1：查询语句的ID
        //参数2：入参
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name","2 or 1=1");
        UserDTO user = sqlSession.selectOne("com.qianfeng.oa.user.dto.UserMapper.queryUserById",map);
        sqlSession.close();
    }

    /**
     * 多条件查询
     */
    @Test
    public void testCase3(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("name", "zhangsan");
        param.put("password","12345");
        UserDTO user = sqlSession.selectOne("com.qianfeng.oa.user.dto.UserMapper.checkUser", param);
        System.out.println(user.getUser_id()+"/" + user.getUser_name() + "/" + user.getUser_email());
        sqlSession.close();
    }

    @Test
    public void testCase4(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_name("zhangsan");
        userDTO.setUser_password("12345");
        UserDTO user = sqlSession.selectOne("com.qianfeng.oa.user.dto.UserMapper.login", userDTO);
        System.out.println(user.getUser_id()+"/" + user.getUser_name() + "/" + user.getUser_email());
        sqlSession.close();
    }

    /**
     * 结果集映射
     */
    @Test
    public void testCase5(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<User2DTO> list = sqlSession.selectList("com.qianfeng.oa.user.dto.UserMapper.queryUserByMap");
        for (User2DTO user:list) {
            System.out.println(user.getEmail() + "/" + user.getUsername());
        }
        sqlSession.close();
    }

    @Test
    public void testCase6(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User2DTO user2DTO = new User2DTO();
        user2DTO.setUsername("lisi2");
        user2DTO.setEmail("333@126.com");
        user2DTO.setPassword("123");
        user2DTO.setSex('1');
        //返回结果是受影响的行数
        int num = sqlSession.insert("com.qianfeng.oa.user.dto.UserMapper.save", user2DTO);
        sqlSession.commit();
        System.out.println(num);
//        sqlSession.close();
    }

    @Test
    public void testCase7(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        DepartmentDTO department = sqlSession.selectOne("com.qianfeng.oa.user.dto.DepartmentMapper.selectById", 1);
        System.out.println(department.getName());
        List<User2DTO> users = department.getUsers();
        for (User2DTO user: users) {
            System.out.println(user.getUsername());
        }
    }

    @Test
    public void testCase8(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User2DTO user = sqlSession.selectOne("com.qianfeng.oa.user.dto.UserMapper.queryUserByName", "张三");
        System.out.println(user.getUsername());
        DepartmentDTO departmentDTO = user.getDepartmentDTO();
        System.out.println(departmentDTO.getName());

    }

    /**
     * 日志输出
     */
    @Test
    public void testCase9(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User2DTO user2DTO = sqlSession.selectOne("com.qianfeng.oa.user.dto.UserMapper.queryUserByName", "张三");
        System.out.println(user2DTO.getUsername());
        sqlSession.close();
    }

    /**
     * 动态sql
     */
    @Test
    public void testCase10(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User2DTO user2DTO = new User2DTO();
        user2DTO.setSex('0');
        //user2DTO.setUserI(1);
        user2DTO.setUsername("张三");
        user2DTO.setEmail("222@123.com");
        List<User2DTO> list = sqlSession.selectList("com.qianfeng.oa.user.dto.UserMapper.queryUserByConditional", user2DTO);
    }

    @Test
    public void testCase11(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        list.add(4);
        List<Object> list1 = sqlSession.selectList("com.qianfeng.oa.user.dto.UserMapper.queryUserByPoint", list);
    }

    @Test
    public void testCase12(){
        String sql = new SQL() {
            {
                SELECT("user_name");
                FROM("oa_user");
                WHERE("user_id=1");

            }
        }.toString();
        System.out.println("sql:  --- "+sql);
    }

    /**
     * 缓存的验证
     */
    @Test
    public void testCase13(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        list.add(4);

        List<User2DTO> user2DTOList = sqlSession.selectList("com.qianfeng.oa.user.dto.UserMapper.queryUserByPoint", list);
        for (User2DTO dto:user2DTOList) {
            System.out.println(dto.getUsername());
        }

        List<User2DTO> user2DTOList2 = sqlSession.selectList("com.qianfeng.oa.user.dto.UserMapper.queryUserByPoint", list);
        for (User2DTO dto:user2DTOList2) {
            System.out.println(dto.getUsername());
        }
        sqlSession.close();
    }

    /**
     * 二级缓存的验证
     */
    @Test
    public void testCase14(){
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        list.add(4);

        List<User2DTO> user2DTOList = sqlSession1.selectList("com.qianfeng.oa.user.dto.UserMapper.queryUserByPoint", list);
        for (User2DTO dto:user2DTOList) {
            System.out.println(dto.getUsername());
        }
        sqlSession1.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        List<User2DTO> user2DTOList2 = sqlSession2.selectList("com.qianfeng.oa.user.dto.UserMapper.queryUserByPoint", list);
        for (User2DTO dto:user2DTOList2) {
            System.out.println(dto.getUsername());
        }
        sqlSession2.close();
    }
}
