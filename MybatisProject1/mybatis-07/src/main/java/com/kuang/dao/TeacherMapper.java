package com.kuang.dao;

import com.kuang.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/25-0:07
 */

public interface TeacherMapper {

    //获取老师
    public Teacher getTeacher(int id);

    //获取指定老师的所有学生和老师信息
    Teacher getTeacher2(@Param("tid") int id);
}
