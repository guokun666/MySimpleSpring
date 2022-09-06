package com.kun.service;

import com.spring.BeanPostProcessor;
import com.spring.annotation.Component;

/**
 * @author guokun
 * @date 2022/9/5 22:46
 */
@Component
public class KunPostProcessorService implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) {
        System.out.println(this + ":" + "postProcessorBeforeInitialization");
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) {
        System.out.println(this + ":" + "postProcessorAfterInitialization");
        return bean;
    }
}
