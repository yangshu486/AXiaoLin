package com.study.common;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * 3.封装token
 * JWTToken 替换 Shiro 原生 Token
 */
public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
        System.out.println(token);
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}