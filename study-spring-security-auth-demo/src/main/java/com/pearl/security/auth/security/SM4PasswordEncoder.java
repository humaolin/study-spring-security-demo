package com.pearl.security.auth.security;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

public class SM4PasswordEncoder implements PasswordEncoder {

    private final SymmetricCrypto sm4;

    public SM4PasswordEncoder(String key) {
        // hutool SmUtil
        // key必须是16字节，即128位：1234567812345678
        this.sm4 = SmUtil.sm4(key.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return sm4.encryptHex(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return sm4.encryptHex(rawPassword.toString()).equals(encodedPassword);
    }
}
