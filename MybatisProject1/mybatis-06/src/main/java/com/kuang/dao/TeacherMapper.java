package com.kuang.dao;

import com.kuang.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/25-0:07
 */

public interface TeacherMapper {

    @Select("select * from teacher where id = #{tid};")
    Teacher getTeacher(@Param("tid") int id);
}
