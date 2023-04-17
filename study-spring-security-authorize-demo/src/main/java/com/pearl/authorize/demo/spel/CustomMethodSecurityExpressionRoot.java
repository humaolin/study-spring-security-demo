package com.pearl.authorize.demo.spel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/11
 */
@Slf4j
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {


    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    private Object filterObject;

    private Object returnObject;

    private Object target;


    /**
     * 自定义表达式
     *
     * @param username 具有权限的用户账号
     * @return 投票结果
     */
    public boolean hasUser(String... username) {
        log.info("进入自定义表达式");
        String name = this.getAuthentication().getName();
        log.info("当前登陆用户:" + name);
        String[] names = username;
        // 循环表达式配置的值
        for (String nameStr : names) {
            if (name.equals(nameStr)) {
                return true;
            }
        }
        log.info("当前登陆用户:" + name + ",没有权限");
        return false;
    }


    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }
    void setThis(Object target) {
        this.target = target;
    }
    @Override
    public Object getThis() {
        return this.target;
    }
}
