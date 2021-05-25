package com.kuang.utils;

import org.junit.Test;

import java.util.UUID;

/**
 * @author Chaoqun Cheng
 * @date 2021-05-2021/5/25-16:17
 */

public class IDUtils {

    //生成随机的id
    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Test
    public void testGetId(){
        System.out.println(getId());
        System.out.println(getId());
        System.out.println(getId());
    }
}
