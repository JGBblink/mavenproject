package com.demo.main.base.jwt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtilRSA {

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

    public static void main(String[] args) throws Exception {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("aa", "xxx");
        String token = JwtUtilRSA.generateToken("sadasdad", 2);
        System.out.println(token);

        Jwt jwt = JwtUtilRSA.parseToken(token);
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
    public static String generateToken(Object data, int expire) throws Exception {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(getClaims())
                .setSubject(JSONUtil.toJsonStr(data))
                .setExpiration(DateUtil.offsetMinute(new Date(), expire))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256);

        return jwtBuilder.compact();
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public static Jwt parseToken(String token) throws Exception {
        Jwt jwt = Jwts.parser()
                .setSigningKey(getPublicKey())
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
    private static PrivateKey getPrivateKey() throws Exception {
        byte[] bytes = readFile("/Users/cjin/source/my_private_workspace/mavenproject/demo_main/src/main/resources/rsa/rsa_private_key_pkcs8.pem");
        byte[] decode = Base64.decode(bytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decode);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * 获取公钥
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey() throws Exception {
        byte[] bytes = readFile("/Users/cjin/source/my_private_workspace/mavenproject/demo_main/src/main/resources/rsa/rsa_public_key8.pem");
        byte[] decode = Base64.decode(bytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decode);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    private static byte[] readFile(String filePath) throws Exception {
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
