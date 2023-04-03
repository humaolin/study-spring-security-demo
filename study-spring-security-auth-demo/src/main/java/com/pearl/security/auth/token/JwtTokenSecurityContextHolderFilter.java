package com.pearl.security.auth.token;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import com.pearl.security.auth.security.PearlUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/30
 */
public class JwtTokenSecurityContextHolderFilter extends GenericFilterBean {
    private static final String FILTER_APPLIED = JwtTokenSecurityContextHolderFilter.class.getName() + ".APPLIED";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            // 已执行过，直接放行
            if (request.getAttribute(FILTER_APPLIED) != null) {
                chain.doFilter(request, response);
            } else {
                // 响应数据
                response.setContentType("application/json;charset=utf-8"); // 返回JSON
                response.setStatus(HttpStatus.OK.value());  // 状态码 200
                Map<String, Object> result = new HashMap<>(); // 返回结果
                result.put("code", 401);
                // 设置已执行标记
                request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
                // 请求头中获取 token
                String token = request.getHeader(AuthenticationConstants.TOKEN_HEADER);
                if (StrUtil.isNotEmpty(token)) {
                    // JWT验证
                    boolean verify = JWTUtil.verify(token, AuthenticationConstants.JWT_KEY.getBytes());
                    if (!verify) {
                        result.put("msg", "当前令牌验证失败");
                        response.getWriter().write(JSONUtil.toJsonStr(result));
                    }
                    // 已过期
                    final JWT jwt = JWTUtil.parseToken(token);
                    jwt.getHeader(JWTHeader.TYPE);
                    long expireTime = (long) jwt.getPayload("expire_time");
                    if (System.currentTimeMillis() >= expireTime) {
                        result.put("msg", "当前令牌已过期");
                        response.getWriter().write(JSONUtil.toJsonStr(result));
                    }
                    // 取出认证信息并放入SecurityContextHolder
                    Object obj = jwt.getPayload("authentication");
                    JSONObject jsonObject = JSONUtil.parseObj(obj);
                    JSONArray jsonArray = jsonObject.getJSONArray("authorities"); //权限
                    List<String> list = jsonArray.toList(String.class);
                    String[] strings = list.toArray(new String[list.size()]);
                    List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
                    JSONObject principal = JSONUtil.parseObj(jsonObject.get("principal")); // 用户信息
                    PearlUserDetails userDetails = new PearlUserDetails(
                            principal.getStr("username"),
                            principal.getStr("password"),
                            "phone",
                            authorityList,
                            true,
                            true,
                            true,
                            true
                    );
                    Object details = jsonObject.get("details");// details (远程地址、SessionID)
                    UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(userDetails, null, authorityList);
                    authentication.setDetails(details);
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);
                }
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            SecurityContextHolder.clearContext();
            request.removeAttribute(FILTER_APPLIED);
        }

    }
}
