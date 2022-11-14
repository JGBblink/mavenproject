package com.demo.main.config.auth;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.main.domain.Role;
import com.demo.main.domain.User;
import com.demo.main.domain.UserRole;
import com.demo.main.mapper.RoleMapper;
import com.demo.main.mapper.UserMapper;
import com.demo.main.mapper.UserRoleMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return queryUser(username);
    }

    private UserDetails queryUser(String username) {
        // https://blog.csdn.net/weixin_43461520/article/details/107941983
        // https://www.jianshu.com/p/a65f883de0c1
        User user = userMapper.selectOne(new QueryWrapper<User>().eq(true, "username", username));
        String encode = passwordEncoder.encode(user.getPassword());
        List<Role> roles = userRoleMapper.selectRolesBYUserName(user.getUsername());
        return new org.springframework.security.core.userdetails.User(username, encode,
                roles.stream().map(e->new SimpleGrantedAuthority(e.getRoleName())).collect(Collectors.toList()));
    }
}
