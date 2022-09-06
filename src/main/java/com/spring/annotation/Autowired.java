package com.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author guokun
 * @date 2022/9/5 21:15
 * 运行时字段注解
 *  依赖注入(当前只考虑字段属性)
 *  先通过类型再通过字段名去获取单例bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {
}
