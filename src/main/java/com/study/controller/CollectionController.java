package com.study.controller;

import com.study.entity.Blog;
import com.study.entity.Collection;
import com.study.entity.User;
import com.study.util.Result;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/collection")
public class CollectionController {
    @Resource
    MongoTemplate mongoTemplate;


    /**
     * 查询所有文集
     * @param user
     * @return
     */
    @RequestMapping("/findAllCollection")
    public Result Collection(@RequestBody User user){
        String username = user.getUsername();
        Query query = new Query(Criteria.where("author").is(username));
        List<Collection> collection = mongoTemplate.find(query,Collection.class);
        return Result.succ(200,"查询成功",collection);
    }

    @RequestMapping("/addCollection")
    public void addCollection(@RequestBody Collection collection){
        Collection collection1 = new Collection();
        collection1.setAuthor(collection.getAuthor());
        collection1.setCollection(collection.getCollection());
         mongoTemplate.insert(collection1);

    }
    @RequestMapping("/deleteCollection")
    public void deleteCollection(@RequestBody Collection collection){
        Query query = Query.query(Criteria.where("collection").is(collection.getCollection()));
        mongoTemplate.remove(query,"collection");
    }
}
