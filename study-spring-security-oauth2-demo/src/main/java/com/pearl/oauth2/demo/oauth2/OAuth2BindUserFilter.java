package com.pearl.oauth2.demo.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.One;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/24
 */
public class OAuth2BindUserFilter extends OncePerRequestFilter {

    public static final String DEFAULT_FILTER_PROCESSES_URI = "/login/oauth2/code/*";

    private static final String AUTHORIZATION_REQUEST_NOT_FOUND_ERROR_CODE = "authorization_request_not_found";

    private static final String CLIENT_REGISTRATION_NOT_FOUND_ERROR_CODE = "client_registration_not_found";
    private ClientRegistrationRepository clientRegistrationRepository;
    protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private RequestMatcher requiresAuthenticationRequestMatcher;

    private OAuth2AuthorizedClientRepository authorizedClientRepository;
    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
    private Converter<OAuth2LoginAuthenticationToken, OAuth2AuthenticationToken> authenticationResultConverter;

    public OAuth2BindUserFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService) {
        this(clientRegistrationRepository, authorizedClientService, "/bind/oauth2/code/*");
    }

    public OAuth2BindUserFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService, String filterProcessesUrl) {
        this(clientRegistrationRepository, (OAuth2AuthorizedClientRepository)(new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService)), filterProcessesUrl);
    }

    public OAuth2BindUserFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, String filterProcessesUrl) {
        this.authorizationRequestRepository = new HttpSessionOAuth2AuthorizationRequestRepository();
        this.authenticationResultConverter = this::createAuthenticationResult;
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        Assert.notNull(authorizedClientRepository, "authorizedClientRepository cannot be null");
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientRepository = authorizedClientRepository;
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filterProcessesUrl));
    }

    public final void setRequiresAuthenticationRequestMatcher(RequestMatcher requestMatcher) {
        Assert.notNull(requestMatcher, "requestMatcher cannot be null");
        this.requiresAuthenticationRequestMatcher = requestMatcher;
    }

    public final void setAuthorizationRequestRepository(AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository) {
        Assert.notNull(authorizationRequestRepository, "authorizationRequestRepository cannot be null");
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    private OAuth2AuthenticationToken createAuthenticationResult(OAuth2LoginAuthenticationToken authenticationResult) {
        return new OAuth2AuthenticationToken(authenticationResult.getPrincipal(), authenticationResult.getAuthorities(), authenticationResult.getClientRegistration().getRegistrationId());
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (this.requiresAuthenticationRequestMatcher.matches(request)) {
            return true;
        } else {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace(LogMessage.format("Did not match request to %s", this.requiresAuthenticationRequestMatcher));
            }
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.requiresAuthentication(request, response)) {
            filterChain.doFilter(request, response);
        }else {
            MultiValueMap<String, String> params = OAuth2AuthorizationResponseUtils.toMultiMap(request.getParameterMap());
            if (!OAuth2AuthorizationResponseUtils.isAuthorizationResponse(params)) {
                OAuth2Error oauth2Error = new OAuth2Error("invalid_request");
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            } else {
                OAuth2AuthorizationRequest authorizationRequest = this.authorizationRequestRepository.removeAuthorizationRequest(request, response);
                if (authorizationRequest == null) {
                    OAuth2Error oauth2Error = new OAuth2Error("authorization_request_not_found");
                    throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
                } else {
                    String registrationId = (String)authorizationRequest.getAttribute("registration_id");
                    ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);
                    if (clientRegistration == null) {
                        OAuth2Error oauth2Error = new OAuth2Error("client_registration_not_found", "Client Registration not found with Id: " + registrationId, (String)null);
                        throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
                    } else {
                        String redirectUri = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request)).replaceQuery((String)null).build().toUriString();
                        OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponseUtils.convert(params, redirectUri);
                        Object authenticationDetails = this.authenticationDetailsSource.buildDetails(request);
                        OAuth2LoginAuthenticationToken authenticationRequest = new OAuth2LoginAuthenticationToken(clientRegistration, new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));
                        authenticationRequest.setDetails(authenticationDetails);
                        // OAuth2LoginAuthenticationToken authenticationResult = (OAuth2LoginAuthenticationToken)this.getAuthenticationManager().authenticate(authenticationRequest);
                        //  OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken)this.authenticationResultConverter.convert(authenticationResult);
                        // Assert.notNull(oauth2Authentication, "authentication result cannot be null");
                        //  oauth2Authentication.setDetails(authenticationDetails);
                        //OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(authenticationResult.getClientRegistration(), oauth2Authentication.getName(), authenticationResult.getAccessToken(), authenticationResult.getRefreshToken());
                        //this.authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request, response);
                        //return oauth2Authentication;
                    }
                }
            }
        }
    }
}
