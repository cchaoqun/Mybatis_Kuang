package com.kuang.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/25-0:05
 */
@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> students;
}
