package com.pearl.oauth2.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class OAuth2LoginSecurityConfig {

    /**
     * 完整配置选项
     * https://gitee.com/profile/account_information
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
/*        http.oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(this.clientRegistrationRepository())
                        .authorizedClientRepository(this.authorizedClientRepository())
                        .authorizedClientService(this.authorizedClientService())
                        .loginPage("/login")
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri(this.authorizationRequestBaseUri())
                                .authorizationRequestRepository(this.authorizationRequestRepository())
                                .authorizationRequestResolver(this.authorizationRequestResolver())
                        )
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri(this.authorizationResponseBaseUri())
                        )
                        .tokenEndpoint(token -> token
                                .accessTokenResponseClient(this.accessTokenResponseClient())
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userAuthoritiesMapper(this.userAuthoritiesMapper())
                                .userService(this.oauth2UserService())
                                .oidcUserService(this.oidcUserService())
                        )
                );*/
        // authorizeHttpRequests：指定多个授权规则，按照顺序
        http.authorizeHttpRequests()
                .anyRequest().authenticated();
        // 开启表单登录
        http.formLogin();
        // 开启 OAuth2 登录
        http.oauth2Login();
        // 开启Basic认证
        http.httpBasic();
        // 关闭 CSRF
        http.csrf().disable();
        return http.build();
    }


/*    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // authorizeHttpRequests：指定多个授权规则，按照顺序
        http.authorizeHttpRequests()
                .anyRequest().authenticated();
        // 开启表单登录
        http.formLogin();
        // 开启 OAuth2 登录
        http.oauth2Login();
        // 开启Basic认证
        http.httpBasic();
        // 关闭 CSRF
        http.csrf().disable();
        return http.build();
    }*/

/*	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
        // Github登录
        ClientRegistration github = ClientRegistration.withRegistrationId("github")
                .clientId("github-client-id")
                .clientSecret("github-client-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("read:user")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .clientName("Github")
                .build();
        // 码云登录
        ClientRegistration gitee = ClientRegistration.withRegistrationId("gitee")
                .clientId("29f5c36c2f4576478e7acd9de476357cc225e7c7683b86510d29072276efc0e2")
                .clientSecret("767bf7931269eaa9cca8a3c01fcf23b65c423d1dc99fb6db6d5c61f30a38caa2")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("user_info")
                .authorizationUri("https://gitee.com/oauth/authorize")
                .tokenUri("https://gitee.com/oauth/token")
                .userInfoUri("https://gitee.com/api/v5/user")
                .userNameAttributeName("login") // 用户
                .clientName("Gitee")
                .build();
		return new InMemoryClientRegistrationRepository(github,gitee);
	}*/
}