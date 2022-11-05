package com.study.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "blog")
public class Blog {
    @Id
    private ObjectId id;
    @Field("title")
    private String title;
    @Field("content")
    private String content;
    @Field("collection")
    private String collection;
    @Field("author")
    private String author;
/**
 * 更新时间
 */
    @Field("update_time")
    @LastModifiedDate
    private Date updateTime;


}
