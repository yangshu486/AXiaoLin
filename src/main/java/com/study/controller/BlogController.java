package com.study.controller;

import com.study.entity.Blog;
import com.study.entity.Blog1;
import com.study.entity.Collection;
import com.study.entity.User;
import com.study.util.Result;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
//        String filepath = "http://localhost:8181/image/";
//            String filepath = "http://121.4.170.191:8181/image/";
    /**
     * 保存图片
     * @param file
     * @return
     */
    @PostMapping("/addImage")
    public HashMap addImage(@RequestParam(name = "file[]")MultipartFile file) {
        HashMap map = new HashMap();
        //文件上传
        if (!file.isEmpty()) {
            try {
                //图片命名
                String fileName=file.getOriginalFilename();
//                String filePath="D:\\test\\image";
                String filePath="/www/wwwroot/yuan/image";
                File newFile = new File(filePath,fileName);
//                String url=filepath+fileName;
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(newFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                map.put("name",fileName);

//                map.put("url","http://localhost:8181/img/"+fileName);
                map.put("url","http://121.4.170.191:8181/img/"+fileName);
                return map;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return map;
            } catch (IOException e) {
                e.printStackTrace();
                return map;
            }
        }
       return map;
    }

    /**
     * 添加blog
     */
    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("/addBlog")
    public void addBlog(@RequestBody Blog blog){
        Blog blog1 = new Blog();
        blog1.setTitle(blog.getTitle());
        blog1.setContent(blog.getContent());
        blog1.setCollection(blog.getCollection());
        blog1.setAuthor(blog.getAuthor());
//        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String datetime = tempDate.format(new Date());
        Date date = new Date();
        blog1.setUpdateTime(date);
        mongoTemplate.save(blog1,"blog");
    }

    /**
     * 查询集合
     * @return
     */


    /**
     * 查询该类中的所有文章
     * @return
     */
    @RequestMapping("/findAllEssay")
    public Result findAllEssay(@RequestBody Collection collection){
        Query query = new Query();
        query.addCriteria(Criteria.where("collection").is(collection.getCollection()));
        query.addCriteria(Criteria.where("author").is(collection.getAuthor()));
        List<Blog> blog = mongoTemplate.find(query,Blog.class);
        List<Blog1> blog2 =new ArrayList<Blog1>();
        for (int i = 0; i < blog.size(); i++) {
            Blog1 blog1 = new Blog1();
            blog1.setId( blog.get(i).getId().toString());
            blog1.setTitle(blog.get(i).getTitle());
            blog1.setContent(blog.get(i).getContent());
            blog1.setUpdateTime(blog.get(i).getUpdateTime());
            blog2.add(blog1);
        }
        return Result.succ(200,"查询成功",blog2);
    }

    /**
     * 查询blog
     */
    @RequestMapping("/findEssay/{id}")
    public Result findEssay(@PathVariable("id") ObjectId id){
//        System.out.println(blog.getId());
//         ObjectId id  = blog.getId();
        Blog blog1= mongoTemplate.findById(new ObjectId(String.valueOf(id)), Blog.class, "blog");
        return Result.succ(200,"查询成功",blog1);
    }

    /**
     * 查询所有文章
     * @return
     */
    @RequestMapping("/findAllBlog")
    public Result findAllBlog(@RequestBody User user){
        String username = user.getUsername();
        Query query = new Query(Criteria.where("author").is(username));
        List<Blog> blog = mongoTemplate.find(query,Blog.class);
        List<Blog1> blog2 =new ArrayList<Blog1>();
        for (int i = 0; i < blog.size(); i++) {
            Blog1 blog1 = new Blog1();
            blog1.setId( blog.get(i).getId().toString());
            blog1.setTitle(blog.get(i).getTitle());
            blog1.setContent(blog.get(i).getContent());
            blog1.setUpdateTime(blog.get(i).getUpdateTime());
            blog2.add(blog1);
        }
        return Result.succ(200,"查询成功",blog2);
    }

    @RequestMapping("/deleteEssay")
    public void deleteEssay(@RequestBody Blog blog){
        ObjectId id = blog.getId();
        System.out.println(id);
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Blog.class);
    }

}
