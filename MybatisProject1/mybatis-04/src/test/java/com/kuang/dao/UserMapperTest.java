package com.kuang.dao;

import com.kuang.pojo.User;
import com.kuang.utils.MybatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/24-13:59
 */

public class UserMapperTest {

    static Logger logger = Logger.getLogger(UserMapperTest.class);

    @Test
    public void getUserByRowBounds(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        //RowBounds实现
        RowBounds rowBounds = new RowBounds(1, 2);

        //通过java代码层面实现分页
        List<User> userList = sqlSession.selectList("com.kuang.dao.UserMapper.getUserByRowBounds", null, rowBounds);
        for(User user:userList){
            System.out.println(user);
        }
        sqlSession.close();


    }

    @Test
    public void getUserByLimit(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("startIndex",1);
        map.put("pageSize",2);

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserByLimit(map);

        System.out.println(userList);


        sqlSession.close();
    }



    @Test
    public void getUserById() {
        //获取SqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //方式一: getMapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById(1);
        System.out.println(user);
        //关闭sqlSession
        sqlSession.close();
    }

    @Test
    public void testLog4J(){
        logger.info("info:进入了testLog4J");
        logger.debug("debug:进入了testLog4J");
        logger.error("error:进入了testLog4J");
    }




}