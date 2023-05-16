package com.pearl.authserver.demo.handler;

import com.pearl.authserver.demo.response.R;
import com.pearl.authserver.demo.response.ResponseUtils;
import com.pearl.authserver.demo.response.ResultCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class TokenEndpointAuthenticationFailureHandler  implements AuthenticationFailureHandler {

    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        OAuth2Error error = ((OAuth2AuthenticationException)exception).getError();
        OAuth2Error errorResponse = new OAuth2Error(error.getErrorCode());
        R<OAuth2Error> result = R.response(ResultCodeEnum.FAIL, errorResponse);
        ResponseUtils.buildResponse(response, result, HttpStatus.BAD_REQUEST);
        //this.errorHttpResponseConverter.write(errorResponse, (MediaType)null, httpResponse);
    }
}
