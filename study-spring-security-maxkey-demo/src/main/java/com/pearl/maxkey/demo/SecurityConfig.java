package com.pearl.maxkey.demo;

import org.opensaml.security.x509.X509Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.File;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    @Autowired
    RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;

    @Bean
    SecurityFilterChain samlSecurityFilterChain(HttpSecurity http) throws Exception {
        // 添加SP元数据查询
        DefaultRelyingPartyRegistrationResolver relyingPartyRegistrationResolver =
                new DefaultRelyingPartyRegistrationResolver(this.relyingPartyRegistrationRepository);
        Saml2MetadataFilter filter = new Saml2MetadataFilter(
                relyingPartyRegistrationResolver,
                new OpenSamlMetadataResolver());
        http.addFilterBefore(filter, Saml2WebSsoAuthenticationFilter.class);
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        // 开启 SAML 登录登出
        http.saml2Login(withDefaults());
        http.saml2Logout(withDefaults());
        return http.build();
    }

    /**
     * 断言方和依赖方的元数据配置
     * 如依赖方在向断言方请求认证时应使用的SSO端点的位置
     */
/*    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrations() throws Exception {
        RelyingPartyRegistration registration = RelyingPartyRegistration
                .withRegistrationId("keycloak") // 依赖方的实体ID，任意的值，你可以选择它来区分不同的注册
                // 断言方的配置元数据
                .assertingPartyDetails(party -> party
                                .encryptionX509Credentials(saml2X509Credentials ->
                                                saml2X509Credentials.add()
                                        )
                        .wantAuthnRequestsSigned(true)
                        .entityId("http://sso.maxkey.top:9527/sign/saml")
                        // 单点登录跳转地址
                        .singleSignOnServiceLocation("http://sso.maxkey.top/sign/authz/saml20/864487223259561984")
                        //.verificationX509Credentials(c -> c.add(credential))
                )
                .build();
        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }*/
}