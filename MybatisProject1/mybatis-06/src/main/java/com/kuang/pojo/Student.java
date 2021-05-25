package com.kuang.pojo;

import lombok.Data;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/25-0:05
 */

@Data
public class Student {
    private int id;
    private String name;
    //学生需要关联一个学生
    private Teacher teacher;
}
