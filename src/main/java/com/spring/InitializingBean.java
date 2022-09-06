package com.spring;

/**
 * @author guokun
 * @date 2022/9/5 21:43
 */
public interface InitializingBean {
    /**
     * 创建Bean实例后，初始化前调用该方法
     */
    void afterPropertiesSet();
}
