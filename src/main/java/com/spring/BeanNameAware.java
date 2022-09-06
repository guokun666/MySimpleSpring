package com.spring;

/**
 * @author guokun
 * @date 2022/9/6 22:32
 */
public interface BeanNameAware extends Aware{
    /**
     * 设置Bean的BeanName属性
     */
    void setBeanName(String name);
}
