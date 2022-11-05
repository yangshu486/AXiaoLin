package com.study.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {

    //    邮件接收
    private String to;
    //    主题
    private String Subject;


}