package com.kuang.dao;

import com.kuang.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/24-13:49
 */

public interface UserMapper {

    List<User> getUserLike(String value);



    //查询全部用户
    List<User> getUserList();

    //根据id查询用户
    User getUserById(int id);

    User getUserById2(Map<String, Object> map);


    //insert一个用户
    int addUser(User user);

    //万能Map
    int addUser2(Map<String, Object> map);

    //update
    int updateUser(User user);

    //delete
    int deleteUserById(int id);

}
