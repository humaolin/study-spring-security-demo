package com.pearl.authserver.demo.jose;

import cn.hutool.core.lang.UUID;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;


public class JJwtTest {
    public static void main(String[] args) {
        // 生成JWT
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 签名密钥，实际开发需要从应用程序配置中读取
        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString()) // 唯一ID
                .setSubject("Joe") // 主体
                .claim("username","kunji") // Payload
                .signWith(key)
                .compact();
        System.out.println(token);
        // 解析JWT
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
        System.out.println(claims.toString());
    }
}
