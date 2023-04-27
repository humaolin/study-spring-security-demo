package com.pearl.authserver.demo.jose;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/26
 */
public class NimbusJwtTest {

    public static void main(String[] args) throws JOSEException, ParseException {
        // 创建JWS
        // 1. 创建Payload，存放有效信息
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("username", "zhangsan");
        // 2. 创建JWS
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256),
                new Payload(jsonObject));

        // 3. 创建秘钥，并签名
        byte[] sharedKey = new byte[32];
        new SecureRandom().nextBytes(sharedKey);
        jwsObject.sign(new MACSigner(sharedKey));
        // 4. 序列化为令牌
        String token = jwsObject.serialize();
        System.out.println(token);

        // 解析JWS
        JWSObject plainObject = JWSObject.parse(token);
        System.out.println(plainObject.getHeader());
        System.out.println(plainObject.getPayload());

        // 验签
        MACVerifier macVerifier = new MACVerifier(sharedKey);
        boolean verify = plainObject.verify(macVerifier);
        System.out.println("验签结果：" + verify);
    }
}
