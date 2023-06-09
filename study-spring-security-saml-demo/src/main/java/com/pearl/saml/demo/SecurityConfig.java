package com.pearl.saml.demo;

import org.opensaml.security.x509.X509Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
import java.security.cert.X509Certificate;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    @Autowired
    RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;

    @Bean
    SecurityFilterChain samlSecurityFilterChain(HttpSecurity http) throws Exception {
        DefaultRelyingPartyRegistrationResolver relyingPartyRegistrationResolver =
                new DefaultRelyingPartyRegistrationResolver(this.relyingPartyRegistrationRepository);
        Saml2MetadataFilter filter = new Saml2MetadataFilter(
                relyingPartyRegistrationResolver,
                new OpenSamlMetadataResolver());
        http.addFilterBefore(filter, Saml2WebSsoAuthenticationFilter.class);

        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.saml2Login(withDefaults());
        http.saml2Logout(withDefaults());
        return http.build();
    }

    /**
     * 内存存储用户
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("123456")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }


    /**
     * 断言方和依赖方的元数据配置
     * 如依赖方在向断言方请求认证时应使用的SSO端点的位置
     */
/*    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrations() throws Exception {
        // X509Certificate certificate= X509Support.decodeCertificate(new File("E:\\TD\\github\\study-spring-security-demo\\study-spring-security-saml-demo\\src\\main\\resources\\keystore.jks"));
        // Saml2X509Credential credential = Saml2X509Credential.verification(certificate);
        RelyingPartyRegistration registration = RelyingPartyRegistration
                .withRegistrationId("spring") // 依赖方的实体ID，任意的值，你可以选择它来区分不同的注册
                // 断言方的配置元数据
                .assertingPartyDetails(party -> party
                        .entityId("https://idp.example.com/issuer")
                        // 单点登录跳转地址
                        .singleSignOnServiceLocation("http://localhost:8080/")
                        .wantAuthnRequestsSigned(false)
                        //.verificationX509Credentials(c -> c.add(credential))
                )
                .build();
        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }*/
}