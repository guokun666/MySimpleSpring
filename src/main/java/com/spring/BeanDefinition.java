package com.spring;

/**
 * @author guokun
 * @date 2022/9/5 20:06
 */
public class BeanDefinition {

    private Class<?> type;
    private String scope;
    private boolean isLazy;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
