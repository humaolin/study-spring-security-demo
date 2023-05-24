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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
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
     * 基于数据库存授权信息
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository clientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, clientRepository);
    }

    /**
     * 基于数据库存储用户、客户端授权关联信息
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository clientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, clientRepository);
    }
    /**
     * 客户端配置，基于内存
     */
/*    @Bean
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
    }*/

    /**
     * 客户端配置，基于数据库存储
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        JdbcRegisteredClientRepository jdbcRegisteredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        jdbcRegisteredClientRepository.save(createDefaultClient());// 初始化时可以直接保存一个客户端到数据库中
        return jdbcRegisteredClientRepository;
    }


    private RegisteredClient createDefaultClient() {
        // 1. 创建客户端配置
        ClientSettings clientSettings = ClientSettings.builder()
                .requireAuthorizationConsent(true) // 需要授权同意，false表示自动授权（静默授权）
                .requireProofKey(false) // 是否需要需要验证密钥
                .settings(map -> { // 添加自定义配置项
                    map.put("user_info_url", "https://www.baidu.com/v1/user-info");
                })
                .build();
        // 2. 创建令牌配置
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(30)) // 访问令牌有效期（默认5分钟）
                .refreshTokenTimeToLive(Duration.ofDays(1)) // 刷新令牌有效期（默认60分钟）
                .authorizationCodeTimeToLive(Duration.ofMinutes(10)) // 授权码有效期（默认5分钟）
                .build();
        // 3. 创建客户端
        return RegisteredClient.withId("123456") // ID
                .clientName("测试客户端")
                .clientId("client")
                .clientSecret(new BCryptPasswordEncoder().encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)// 密码模式
                .redirectUri("http://127.0.0.1:8080/callback")
                .scope("user_info")
                .scope(OidcScopes.OPENID) // OIDC
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
    }


    /**
     * 配置Spring授权服务器
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
/*        return AuthorizationServerSettings.builder()
                // 发行者（一般设置为访问域名）
                .issuer("https://www.qq.com") // 默认 http://localhost:8080
                // 授权端点路径
                .authorizationEndpoint("/v2/oidc/authorize") // 默认 /oauth2/authorize
                // 令牌端点路径
                .tokenEndpoint("/v2/oidc/token") // 默认 /oauth2/token
                // 令牌内省端点
                .tokenIntrospectionEndpoint("/oidc/introspect") // 默认 /oauth2/introspect
                // 令牌销毁端点
                .tokenRevocationEndpoint("/oidc/revoke")// 默认 /oauth2/revoke
                // OIDC 用户信息端点
                .oidcUserInfoEndpoint("/user-info")
                .build();*/
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

    public static void main(String[] args) {
        String secret = new BCryptPasswordEncoder().encode("123456");
        System.out.println(secret);
    }
}
