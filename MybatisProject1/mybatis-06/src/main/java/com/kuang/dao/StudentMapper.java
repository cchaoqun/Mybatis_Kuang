package com.kuang.dao;

import com.kuang.pojo.Student;

import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/25-0:07
 */

public interface StudentMapper {
    //查询所有学生信息, 以及对应的老师的信息
    public List<Student> getStudent();

    public List<Student> getStudent2();
}
