package com.spring;


/**
 * @author guokun
 * @date 2022/9/5 21:53
 */
public interface BeanPostProcessor {

    /**
     * bean初始化前置处理
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessorBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * bean初始化后置处理
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessorAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
