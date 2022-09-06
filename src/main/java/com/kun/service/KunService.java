package com.kun.service;

import com.spring.BeanNameAware;
import com.spring.InitializingBean;
import com.spring.annotation.Component;

/**
 * @author guokun
 * @date 2022/9/5 17:15
 */
@Component("kunService")
public class KunService implements InitializingBean, BeanNameAware {
    private String beanName;

    public void test() {
        System.out.println("kunService test()");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println(this + ":" + "afterPropertiesSet");
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
