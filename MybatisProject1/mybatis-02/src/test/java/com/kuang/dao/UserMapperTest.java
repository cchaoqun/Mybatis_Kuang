package com.kuang.dao;

import com.kuang.pojo.User;
import com.kuang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/24-13:59
 */

public class UserMapperTest {

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


}