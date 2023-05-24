package com.example.study.authserver.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/27
 */
@Configuration
public class JwtTokenConfig {

/*    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(OidcUserInfoService oidcUserInfoService) {
        return (context) -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                OidcUserInfo userInfo = oidcUserInfoService.loadUser(
                        context.getPrincipal().getName(),context.getAuthorizedScopes());
                context.getClaims().claims(claims ->
                        claims.putAll(userInfo.getClaims()));
            }
        };
    }*/


/*    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        accessTokenGenerator.setAccessTokenCustomizer(accessTokenCustomizer());
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer() {
        return context -> {
            OAuth2TokenClaimsSet.Builder claims = context.getClaims();
            claims.claim("123","222");
            // Customize claims

        };
    }*/
}
