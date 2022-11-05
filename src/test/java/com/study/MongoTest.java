package com.study;

import com.study.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoTest extends DemoApplicationTests {


    @Autowired(required = false)
    MongoTemplate mongoTemplate;
    //    创建集合
    @Test
    public void testCreateCollection(){
        System.out.println(mongoTemplate.findAll(User.class));
    }
    //    删除集合
    @Test
    public void testDropCollection(){
        mongoTemplate.dropCollection("users");
    }
}
