package com.kuang.dao;

import com.kuang.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/25-18:36
 */

public interface UserMapper {

    //根据id查询用户
    User queryUserById(@Param("id") int id);
    //修改用户
    int updateUser(User user);
}
