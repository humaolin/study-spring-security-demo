package com.pearl.oauth2.demo.oauth2;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pearl.oauth2.demo.entity.User;
import com.pearl.oauth2.demo.entity.UserBindThirdLogin;
import com.pearl.oauth2.demo.security.PearlUserDetails;
import com.pearl.oauth2.demo.service.IUserBindThirdLoginService;
import com.pearl.oauth2.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/23
 */
@Service
public class PearlOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";
    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {
    };
    private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
    private RestOperations restOperations;
    private final IUserBindThirdLoginService bindThirdLoginService;

    private final IUserService userService;

    public PearlOAuth2UserService(IUserBindThirdLoginService bindThirdLoginService, IUserService userService) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
        this.bindThirdLoginService = bindThirdLoginService;
        this.userService = userService;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 校验OAuth2UserRequest、用户信息查询接口不能为空
        Assert.notNull(userRequest, "userRequest cannot be null");
        if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error("missing_user_info_uri", "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        } else {
            // 2. 取出配置的第三方平台用户ID
            String userIdAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
            if (!StringUtils.hasText(userIdAttributeName)) {
                OAuth2Error oauth2Error = new OAuth2Error("missing_user_name_attribute", "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String) null);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            } else {
                // 3. 查询第三方平台用户信息
                RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);
                ResponseEntity<Map<String, Object>> response = this.getResponse(userRequest, request);
                Map<String, Object> userAttributes = response.getBody(); // 用户信息
                Dict dict = Dict.parse(userAttributes);// 转为 hutool 类，方便取值https://hutool.cn/docs/#/core/%E8%AF%AD%E8%A8%80%E7%89%B9%E6%80%A7/HashMap%E6%89%A9%E5%B1%95-Dict?id=%e7%94%b1%e6%9d%a5
                // 4. 取出对应的第三方平台用户ID
                Assert.notNull(userAttributes, "userAttributes cannot be null");
                String thirdUserId = (String.valueOf(userAttributes.get(userIdAttributeName)));
                if (StrUtil.isEmpty(thirdUserId)) {
                    // 未查询到第三方对应的用户信息，直接创建用户
                    String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 平台标识
                    // 自动创建用户
                    User user = new User();
                    if ("gitee".equals(registrationId)) {
                        // 码云（和github相关字段都是一样的）
                        user.setUserName(dict.getStr("login")); // 登录账号
                        user.setLoginName(dict.getStr("name") + "(注册自码云)");
                    } else if ("github".equals(registrationId)) {
                        // github
                        user.setUserName(dict.getStr("login")); // 登录账号
                        user.setLoginName(dict.getStr("name") + "(注册自GitHub)");
                    }
                    user.setPassword(new BCryptPasswordEncoder().encode("123456")); // 随机密码
                    userService.save(user);// 保存用户
                    // 新增绑定关系
                    UserBindThirdLogin userBindThirdLogin = new UserBindThirdLogin();
                    userBindThirdLogin.setUserId(user.getUserId());
                    userBindThirdLogin.setType(registrationId);
                    userBindThirdLogin.setCreateTime(LocalDateTime.now());
                    userBindThirdLogin.setNickname(dict.getStr("name"));
                    bindThirdLoginService.save(userBindThirdLogin);
                }
                // 5.查询绑定表
                String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 类型
                UserBindThirdLogin userBindThirdLogin = bindThirdLoginService.selectOne(registrationId, thirdUserId);
                Assert.notNull(userBindThirdLogin, "userBindThirdLogin cannot be null");
                // 6. 查询对应的当前平台用户信息
                Long userId = userBindThirdLogin.getUserId();
                User user = userService.getById(userId);
                // 7. 设置权限集合，后续需要数据库查询（授权篇讲解）
                List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
                // 8. 返回UserDetails类型用户
                return new PearlUserDetails(user.getUserId(), user.getUserName(), user.getPhone(), authorityList,
                        true, true, true, true); // 账号状态这里都直接设置为启用，实际业务可以存在数据库中
            }
        }
    }

    private ResponseEntity<Map<String, Object>> getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request) {
        OAuth2Error oauth2Error;
        try {
            return this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        } catch (OAuth2AuthorizationException var6) {
            oauth2Error = var6.getError();
            StringBuilder errorDetails = new StringBuilder();
            errorDetails.append("Error details: [");
            errorDetails.append("UserInfo Uri: ").append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
            errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
            if (oauth2Error.getDescription() != null) {
                errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
            }
            errorDetails.append("]");
            oauth2Error = new OAuth2Error("invalid_user_info_response", "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var6);
        } catch (UnknownContentTypeException var7) {
            String var10000 = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
            String errorMessage = "An error occurred while attempting to retrieve the UserInfo Resource from '" + var10000 + "': response contains invalid content type '" + var7.getContentType().toString() + "'. The UserInfo Response should return a JSON object (content type 'application/json') that contains a collection of name and value pairs of the claims about the authenticated End-User. Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration '" + userRequest.getClientRegistration().getRegistrationId() + "' conforms to the UserInfo Endpoint, as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'";
            oauth2Error = new OAuth2Error("invalid_user_info_response", errorMessage, (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var7);
        } catch (RestClientException var8) {
            oauth2Error = new OAuth2Error("invalid_user_info_response", "An error occurred while attempting to retrieve the UserInfo Resource: " + var8.getMessage(), (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var8);
        }
    }

    public final void setRequestEntityConverter(Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter) {
        Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
        this.requestEntityConverter = requestEntityConverter;
    }

    public final void setRestOperations(RestOperations restOperations) {
        Assert.notNull(restOperations, "restOperations cannot be null");
        this.restOperations = restOperations;
    }
}
