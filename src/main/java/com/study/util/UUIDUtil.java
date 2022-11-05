package com.study.util;

import java.util.Date;
import java.util.UUID;

/**
 * 在spring boot框架下，后台完成的接收图片和存地址到数据库之中的方法
 */
public class UUIDUtil {
    public static String generateUUId() {
        return UUID.randomUUID().toString().replace("-", "") + new Date().getTime();
    }

    public static void main(String[] args) {
        String s = generateUUId();
        System.out.println(s);
    }

}

