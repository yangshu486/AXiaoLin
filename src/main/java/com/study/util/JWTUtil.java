package com.study.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.entity.User;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 2. 编写JwtUtil类
 * JwtUtil类是用来生成token和验校验解码token的。
 *
 * 步骤：
 *
 * 设置密钥和token的有效时间
 * 生成token
 * 校验token
 * 获取token的信息
 * JWT token 工具类，提供JWT生成、校验、获取token存储的信息
 */

@Component
public class JWTUtil {
    //token有效时长
    private static final long EXPIRE=24 * 60 * 30 * 1000;
    //token的密钥
    private static final String SECRET="shirojwt";


    public static String createToken(User user) throws UnsupportedEncodingException {
        //token过期时间
        Date date=new Date(System.currentTimeMillis()+EXPIRE);

        //jwt的header部分
        Map<String ,Object> map=new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");

        //使用jwt的api生成token
        String token= JWT.create()
                .withHeader(map)
                .withClaim("username", user.getUsername())//私有声明
                .withExpiresAt(date)//过期时间
                .withIssuedAt(new Date())//签发时间
                .sign(Algorithm.HMAC256(SECRET));//签名
        System.out.println("JWRUtil+这里已经生成了token");
        return token;
    }

    //校验token的有效性，1、token的header和payload是否没改过；2、没有过期
    public static boolean verify(String token){
        try {
            //解密
            JWTVerifier verifier= JWT.require(Algorithm.HMAC256(SECRET)).build();
            verifier.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    //获取登录名
    public static String getUsername(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }

    }
}
