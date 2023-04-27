package com.pearl.authserver.demo.jose;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author TangDan
 * @version 1.0
 * @since 2023/4/27
 */
public class SecurityNimbusJwtTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // JDK 生成RSA非对称秘钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 私钥
        // 生成JWK
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        // 创建JWKSet
        JWKSet jwkSet = new JWKSet(Collections.singletonList(rsaKey));
        // Jwt编码器
        ImmutableJWKSet<SecurityContext> context = new ImmutableJWKSet<>(jwkSet);
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(context);
        // Header
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();
        // Payload
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("http://localhost:8080/")
                .subject("test")
                .id(UUID.randomUUID().toString())
                .claim("scope", List.of("user_info"))
                .build();
        // 编码参数
        JwtEncoderParameters parameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);
        // 进行编码，自动签名，返回JWT对象
        Jwt jwt = jwtEncoder.encode(parameters);
        System.out.println("token:" + jwt.getTokenValue());
    }
}
