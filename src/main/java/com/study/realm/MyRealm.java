package com.study.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.common.JWTToken;
import com.study.entity.User;
import com.study.mapper.UserMapper;
import com.study.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 自定义 ShiroDatabaseRealm 实现 Shiro Realm 的登录控制
 * 重写 Realm 的 supports() 方法是通过 JWT 进行登录判断的关键
 * 因为前文中创建了 JWTToken 用于替换 Shiro 原生 token
 * 所以必须在此方法中显式的进行替换，否则在进行判断时会一直失败
 * 登录的合法验证通常包括 token 是否有效 、用户名是否存在 、密码是否正确
 * 通过 JWTUtil 对前端传入的 token 进行处理获取到用户名
 * 然后使用用户名前往数据库获取到用户密码
 * 再将用户面传入 JWTUtil 进行验证即可
 */
@Service
public class MyRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserMapper userMapper;

    //根据token判断此Authenticator是否使用该realm
    //必须重写不然shiro会报错
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如@RequiresRoles,@RequiresPermissions之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String token = principals.toString();
        String username = JWTUtil.getUsername(token);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.ge("username",username);
        User user = userMapper.selectOne(wrapper);
        //查询数据库来获取用户的角色
//        info.addRole(user.getRole());
        return info;
    }


    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可，在需要用户认证和鉴权的时候才会调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证~~~~~~~");
        String jwt = (String) token.getCredentials();
        String username = null;
        //decode时候出错，可能是token的长度和规定好的不一样了
        try {
            username = JWTUtil.getUsername(jwt);
        } catch (Exception e) {
            throw new AuthenticationException("token非法，不是规范的token，可能被篡改了，或者过期了");
        }
        if (!JWTUtil.verify(jwt) || username == null) {
            throw new AuthenticationException("token认证失效，token错误或者过期，重新登陆");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.ge("username",username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new AuthenticationException("该用户不存在");
        }

        return new SimpleAuthenticationInfo(jwt, jwt, "AccountRealm");
    }
}
