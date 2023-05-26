package com.example.study.authserver.config;

import com.example.study.authserver.handler.JsonAccessDeniedHandler;
import com.example.study.authserver.handler.JsonAuthenticationEntryPoint;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/25
 */
@Configuration(proxyBeanMethods = false)
public class SpringAuthServerConfig {

    /**
     * 授权服务器 SecurityFilterChain
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
        http.exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(new JsonAccessDeniedHandler()) // 权限不足
                        .authenticationEntryPoint(new JsonAuthenticationEntryPoint()) // 未登录
                )
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

    /**
     * 客户端配置，基于内存
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // http://localhost:8080/oauth2/authorize?client_id=client&scope=user_info&state=123456&response_type=code&redirect_uri=http://127.0.0.1:8080/callback
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/callback")
                .scope("user_info")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     * 配置Spring授权服务器
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }


    /**
     * 解码签名访问令牌
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 访问令牌签名
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 私钥
        RSAKey rsaKey = new RSAKey.Builder(publicKey) // 构建为JOSE RSA秘钥
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey); // 创建JWKSet
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 其 key 在启动时生成，用于创建上述 JWKSource
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            // 使用`RSA`算法生成非对称秘钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}
