package com.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.entity.Email;
import com.study.entity.User;
import com.study.mapper.UserMapper;
import com.study.service.UserService;
import com.study.service.impl.UserServiceImpl;
import com.study.util.JWTUtil;
import com.study.util.Result;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/login")
    public Result login(@RequestBody User user) throws UnsupportedEncodingException {
        String username = user.getUsername();
        String password = user.getPassword();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        wrapper.eq("password",password);
        User user1 = userMapper.selectOne(wrapper);
        Assert.notNull(user1,"用户名或密码错误");
        String token= JWTUtil.createToken(user1);
        System.out.println(token);
        return Result.succ(200,"登陆成功",token);
    }
    @RequestMapping("/findUser")
    public Result findUser(@RequestBody User user){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        User user1= userMapper.selectOne(wrapper);
        System.out.println(user1);
        return Result.succ(200,"查询成功",user1);
    }

    @Resource
    private JavaMailSender mailSender;


    /**
     * 生成6位随机数
     */
    public static int randomCode() {
        int  code = (int) ((Math.random() * 9 + 1) * 100000);
        return code;
    }

    /**
     * 发送邮件码
     * @param email
     * @return
     */
    private int code=randomCode();

    @RequestMapping("/sendEmail")
    public Result sendEmail(@RequestBody Email email) {
        //创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置发件人
        message.setFrom("3459049385@qq.com");
        // 设置收件人
        message.setTo( email.getTo());
        // 设置邮件标题
        message.setSubject(email.getSubject());
        message.setText("您好，欢迎来到 XiaoLin , 以下是您的验证码："+ code +" 请尽快注册, 再次感谢您的使用");
        // 发送邮件
        mailSender.send(message);
        return Result.succ(200,"发送成功","nice");
    }
    @RequestMapping("/enroll")
    public Result enroll(@RequestBody User user)  throws UnsupportedEncodingException {
        if (user.getActiveCode() == code||user.getActiveCode()==0){
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username",user.getUsername());
            User user1= userMapper.selectOne(wrapper);
            Assert.isNull(user1,"用户名重复");
            User user2 = new User();
            user2.setName(user.getName());
            user2.setUsername(user.getUsername());
            user2.setPassword(user.getPassword());
            user2.setEmail(user.getEmail());
            userMapper.insert(user2);
            return Result.succ(200,"注册成功","ok");
        }else{
            return Result.succ(400,"验证码错误","no");
        }
    }


    @RequestMapping(path = "/unauthorized/{message}")
    public Result unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return Result.fail(message);
    }
}
