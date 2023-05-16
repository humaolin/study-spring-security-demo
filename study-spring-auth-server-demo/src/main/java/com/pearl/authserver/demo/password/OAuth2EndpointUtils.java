/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pearl.authserver.demo.password;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Utility methods for the OAuth 2.0 Protocol Endpoints.
 *
 * @author Joe Grandja
 * @since 0.0.1
 */
public final class OAuth2EndpointUtils {

	// 令牌申请异常信息规范
	public static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap(parameterMap.size());
		parameterMap.forEach((key, values) -> {
			if (values.length > 0) {
				String[] var3 = values;
				int var4 = values.length;

				for(int var5 = 0; var5 < var4; ++var5) {
					String value = var3[var5];
					parameters.add(key, value);
				}
			}

		});
		return parameters;
	}

	public static Map<String, Object> getParametersIfMatchesAuthorizationCodeGrantRequest(HttpServletRequest request, String... exclusions) {
		if (!matchesAuthorizationCodeGrantRequest(request)) {
			return Collections.emptyMap();
		} else {
			Map<String, Object> parameters = new HashMap(getParameters(request).toSingleValueMap());
			String[] var3 = exclusions;
			int var4 = exclusions.length;

			for(int var5 = 0; var5 < var4; ++var5) {
				String exclusion = var3[var5];
				parameters.remove(exclusion);
			}

			return parameters;
		}
	}

	public static boolean matchesAuthorizationCodeGrantRequest(HttpServletRequest request) {
		return AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(request.getParameter("grant_type")) && request.getParameter("code") != null;
	}

	public static boolean matchesPkceTokenRequest(HttpServletRequest request) {
		return matchesAuthorizationCodeGrantRequest(request) && request.getParameter("code_verifier") != null;
	}

	public static void throwError(String errorCode, String parameterName, String errorUri) {
		OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
		throw new OAuth2AuthenticationException(error);
	}
}
