package com.pearl.authserver.demo.jose;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

public class HutoolJwtTest {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
                put("uid", Integer.parseInt("123"));
                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
            }
        };

        JWTUtil.createToken(map, "1234".getBytes());
        String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9." +
                "U2aQkC2THYV9L0fTN-yBBI7gmo5xhmvMhATtu8v0zEA";

        final JWT jwt = JWTUtil.parseToken(rightToken);
        jwt.getHeader(JWTHeader.TYPE);
        jwt.getPayload("sub");
    }
}
