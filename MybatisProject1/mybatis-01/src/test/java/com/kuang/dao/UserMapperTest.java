package com.kuang.dao;

import com.kuang.pojo.User;
import com.kuang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/24-13:59
 */

public class UserMapperTest {

    @Test
    public void getUserLike(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserLike("%李%");
        System.out.println(userList);

        sqlSession.close();

    }

    @Test
    public void getUserList() {
        //获取SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //方式一: getMapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();

        //方式二
//        List<User> userList = sqlSession.selectList("com.com.kuang.dao.UserDao.getUserList");
        for(User user:userList){
            System.out.println(user);
        }

        //关闭sqlSession
        sqlSession.close();

    }

    @Test
    public void getUserById(){

        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById(2);
        System.out.println(user);

        sqlSession.close();

    }

    @Test
    public void getUserById2(){

        SqlSession sqlSession = MybatisUtils.getSqlSession();

        HashMap<String, Object> map = new HashMap<>();
        map.put("helloId",1);
        map.put("helloName","pwd1");
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById2(map);
        System.out.println(user);

        sqlSession.close();

    }

    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.addUser(new User(6,"name4", "pwd4"));
        if(res>0){
            System.out.println("插入成功");
        }
        //提交事务
        sqlSession.commit();

        sqlSession.close();
    }

    @Test
    public void addUser2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",5);
        map.put("userName","name555");
        map.put("userPwd","pwd555");
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.addUser2(map);

        sqlSession.commit();
        sqlSession.close();

    }

    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.updateUser(new User(5, "name555", "pwd555"));
        if(res>0){
            System.out.println("更新成功");
        }
        sqlSession.commit();


        sqlSession.close();
    }

    @Test
    public void deleteUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.deleteUserById(5);
        if(res>0){
            System.out.println("删除成功");
        }
        sqlSession.commit();
        sqlSession.close();

    }
}