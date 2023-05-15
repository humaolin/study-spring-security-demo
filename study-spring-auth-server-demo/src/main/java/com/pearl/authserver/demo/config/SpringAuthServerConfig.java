package com.pearl.authserver.demo.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.pearl.authserver.demo.handler.*;
import com.pearl.authserver.demo.password.OAuth2AuthenticationPasswordConverter;
import com.pearl.authserver.demo.password.OAuth2AuthorizationPasswordAuthenticationProvider;
import com.pearl.authserver.demo.password.OAuth2ConfigurerUtils;
import com.pearl.authserver.demo.service.OidcUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.authentication.*;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationConsentAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

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
/*    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
        http.exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login"))
                )
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }*/

    /**
     * OIDC授权服务器
     */

    @Autowired
    OidcUserInfoService oidcUserInfoService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // http://localhost:8080/oauth2/authorize?client_id=client&scope=openid email&state=123456&response_type=code&redirect_uri=http://127.0.0.1:8080/callback
        // 授权服务器配置类
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        // 添加自定义授权页面
/*        authorizationServerConfigurer.authorizationEndpoint(endpoint -> {
            endpoint.consentPage("/oauth2/consent");
        });*/
        // 授权端点配置
        /**
         * 	authorizationRequestConverter(): 添加一个 AuthenticationConverter（预处理器），当试图从 HttpServletRequest 中提取 OAuth2授权请求（或consent）到 OAuth2AuthorizationCodeRequestAuthenticationToken 或 OAuth2AuthorizationConsentAuthenticationToken 的实例时使用。
         * authorizationRequestConverters(): 设置 Consumer，提供对默认和（可选）添加的 AuthenticationConverter List 的访问，允许添加、删除或定制特定的 AuthenticationConverter 的能力。
         * authenticationProvider(): 添加一个 AuthenticationProvider（主处理器），用于验证 OAuth2AuthorizationCodeRequestAuthenticationToken 或 OAuth2AuthorizationConsentAuthenticationToken。
         * authenticationProviders(): 设置 Consumer，提供对默认和（可选）添加的 AuthenticationProvider List 的访问，允许添加、删除或定制特定的 AuthenticationProvider。
         * authorizationResponseHandler(): AuthenticationSuccessHandler（后处理器），用于处理 "已认证" 的 OAuth2AuthorizationCodeRequestAuthenticationToken 并返回 OAuth2AuthorizationResponse。
         * errorResponseHandler(): AuthenticationFailureHandler（后处理器），用于处理 OAuth2AuthorizationCodeRequestAuthenticationException，并返回 OAuth2Error 响应。
         * OAuth2AuthorizationEndpointConfigurer 配置 OAuth2AuthorizationEndpointFilter，并将其与 OAuth2 授权服务器 SecurityFilterChain @Bean 注册。OAuth2AuthorizationEndpointFilter 是处理OAuth2 授权请求（和consent）的filter。
         *
         * OAuth2AuthorizationEndpointFilter 配置的默认值如下。
         *
         * AuthenticationConverter — 一个由 OAuth2AuthorizationCodeRequestAuthenticationConverter 和 OAuth2AuthorizationConsentAuthenticationConverter 组成的 DelegatingAuthenticationConverter。
         *
         * AuthenticationManager — 一个由 OAuth2AuthorizationCodeRequestAuthenticationProvider 和 OAuth2AuthorizationConsentAuthenticationProvider 组成的 AuthenticationManager。
         *
         */
        authorizationServerConfigurer
                //.authorizationConsentService()
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint

                                .authorizationRequestConverters(authorizationRequestConvertersConsumer())
                                /*                      .authorizationRequestConverters(authorizationRequestConvertersConsumer)
                                                    .authenticationProvider(authenticationProvider)
                                                    .authenticationProviders(authenticationProvidersConsumer)*/
                                .authenticationProviders(configureAuthenticationValidator())
                                .authorizationResponseHandler(new PearlAuthenticationSuccessHandler()) // 授权成功处理器
                                .errorResponseHandler(new PearlAuthenticationFailureHandler()) // 授权失败处理器
                                .consentPage("/oauth2/consent") // 同意授权页面URI
                );

        authorizationServerConfigurer
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
                                //.accessTokenRequestConverter(accessTokenRequestConverter)
                                .accessTokenRequestConverters(accessTokenRequestConvertersConsumer())
                                /*   .authenticationProvider(authenticationProvider)*/
                                .authenticationProviders(authenticationProvidersConsumer(http))
                                .accessTokenResponseHandler(new MyAuthenticationSuccessHandler())
                                .errorResponseHandler(new MyAuthenticationFailureHandler())
                );
        // 创建用户信息映射器
        Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper = (context) -> {
            OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
            JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
            Set<String> scopes = context.getAuthorization().getAuthorizedScopes();
            return oidcUserInfoService.loadUser(principal.getName(), scopes);
        };
        // 配置用户信息查询
        authorizationServerConfigurer.oidc((oidc) -> oidc
                .userInfoEndpoint((userInfo) -> userInfo
                        .userInfoMapper(userInfoMapper)
                ));
        // 安全配置
        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests((authorize) ->
                        authorize.
                                anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                )
                .apply(authorizationServerConfigurer);
        return http.build();
    }

    private Consumer<List<AuthenticationConverter>> accessTokenRequestConvertersConsumer() {
        return (authorizationRequestConverters) -> {
            authorizationRequestConverters.add(new OAuth2AuthenticationPasswordConverter());
        };
    }

    private Consumer<List<AuthenticationProvider>> authenticationProvidersConsumer(HttpSecurity httpSecurity
    ) {
        JwtGenerator jwtGenerator = OAuth2ConfigurerUtils.getJwtGenerator(httpSecurity);
        return (authenticationProviders) ->
                authenticationProviders.add(new OAuth2AuthorizationPasswordAuthenticationProvider(userDetailsService, jwtGenerator));

    }

    @Autowired
    public UserDetailsService userDetailsService;

    private static List<AuthenticationProvider> createDefaultAuthenticationProviders(HttpSecurity httpSecurity) {
        List<AuthenticationProvider> authenticationProviders = new ArrayList();
        OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
        OAuth2AuthorizationCodeAuthenticationProvider authorizationCodeAuthenticationProvider = new OAuth2AuthorizationCodeAuthenticationProvider(authorizationService, tokenGenerator);
        authenticationProviders.add(authorizationCodeAuthenticationProvider);
        OAuth2RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider = new OAuth2RefreshTokenAuthenticationProvider(authorizationService, tokenGenerator);
        authenticationProviders.add(refreshTokenAuthenticationProvider);
        OAuth2ClientCredentialsAuthenticationProvider clientCredentialsAuthenticationProvider = new OAuth2ClientCredentialsAuthenticationProvider(authorizationService, tokenGenerator);
        authenticationProviders.add(clientCredentialsAuthenticationProvider);
        return authenticationProviders;
    }


    private Consumer<List<AuthenticationProvider>> configureAuthenticationValidator() {
        return (authenticationProviders) ->
                authenticationProviders.forEach((authenticationProvider) -> {
                    if (authenticationProvider instanceof OAuth2AuthorizationCodeRequestAuthenticationProvider) {
                        Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> authenticationValidator =
                                new CustomValidator()
                                        .andThen(OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_SCOPE_VALIDATOR);

                        ((OAuth2AuthorizationCodeRequestAuthenticationProvider) authenticationProvider)
                                .setAuthenticationValidator(authenticationValidator);
                    }
                });
    }

    private Consumer<List<AuthenticationConverter>> authorizationRequestConvertersConsumer() {
        return (authorizationRequestConverters) -> {
            authorizationRequestConverters.add(new PearlAuthenticationConverter());
        };
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
                .clientSecret("{noop}secret")
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
