package com.kuang.dao;

import com.kuang.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/24-13:49
 */

public interface UserMapper {

    //根据id查询用户
    User getUserById(@Param("cid")int id);

    //分页
    List<User> getUserByLimit(Map<String, Integer> map);

    //分页2
    List<User> getUserByRowBounds();


}
