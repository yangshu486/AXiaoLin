package com.study.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class Blog1 {
    private String id;
    private String title;
    private String content;
    private String collection;
    private String author;
    @LastModifiedDate
    private Date updateTime;
}
