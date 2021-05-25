package com.kuang.dao;

import com.kuang.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/24-13:49
 */

public interface UserMapper {
    //查询所有
    @Select("select * from user;")
    List<User> getUsers();

    //根据id查询
    //方法存在多个参数,所有的参数都需加上 @Param("")
    @Select("select * from user where id=#{id};")
    User getUserById(@Param("id") int id, @Param("name") String name);


    //添加
    @Insert("insert into user(id,name,pwd) values(#{id}, #{name}, #{password})")
    int addUser(User user);

    //更新
    @Update("update user set name=#{name},pwd=#{password} where id=#{id};")
    int updateUser(User user);

    //删除
    @Delete("delete from user where id = #{uid};")
    int deleteUser(@Param("uid")int id);
}
