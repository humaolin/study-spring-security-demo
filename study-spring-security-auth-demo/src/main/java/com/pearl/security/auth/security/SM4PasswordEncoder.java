package com.pearl.security.auth.security;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SM4PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // SM4 加密
        SymmetricCrypto sm4 = SmUtil.sm4();
        String encryptHex = sm4.encryptHex(rawPassword.toString());
        return sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        SymmetricCrypto sm4 = SmUtil.sm4();
        String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}
