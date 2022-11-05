package com.study.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 配置自动填充
 */
@Component
public class MyMateObjectHandler implements MetaObjectHandler {

    //使用Mybatis-plus实现添加操作时 这个方法执行
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
        this.setFieldValByName("version",1,metaObject);
    }
    //使用Mybatis-plus实现修改操作时 这个方法执行
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",new Date(),metaObject);

    }
}
