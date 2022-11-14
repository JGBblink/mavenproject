package com.demo.main.base.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtilAES {

    /**
     * JWT包含三部分
     * 1.header     : 包含声明
     * 2.payload    : 存放有效信息(claims、id、subject)
     *  id：唯一标识
     *  claims：附加信息，可以作为验证token标识
     *  subject：附带信息
     * 3.signature  : 签证信息
     */

    /**
     * 私钥，可以存储在配置文件中，不能泄漏
     */
    private static final String PRIVATE_KEY = "Dtta3z2!SJKFm0vogJGr8^4i0!nh^%&LiUF@%65y0cZrqyfvxJ$mG*EZXjIn^A";
    private static final String SHA256 = "HmacSHA256";

    public static void main(String[] args) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("aa", "xxx");
        String token = JwtUtilAES.generateToken("sadasdad", 2);
        System.out.println(token);

        Jwt jwt = JwtUtilAES.parseToken(token);
        System.out.println(jwt.getHeader());
        System.out.println(jwt.getBody());

    }

    /**
     * 获取token
     *
     * @param data
     * @param expire : 过期时间（分钟）
     * @return
     */
    public static String generateToken(Object data, int expire) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(getClaims())
                .setSubject(JSONUtil.toJsonStr(data))
                .setExpiration(DateUtil.offsetMinute(new Date(), expire))
                .signWith(getPrivateKey(), SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static Jwt parseToken(String token) {
        Jwt jwt = Jwts.parser()
                .setSigningKey(getPrivateKey())
                .parse(token);
        return jwt;
    }

    /**
     * 设置附载信息
     *
     * @return
     */
    private static Claims getClaims() {
        return new DefaultClaims();
    }

    /**
     * 获取私钥
     *
     * @return
     */
    private static SecretKey getPrivateKey() {
        byte[] decode = Base64.decode(PRIVATE_KEY);
        return new SecretKeySpec(decode, 0, decode.length, SHA256);
    }
}
