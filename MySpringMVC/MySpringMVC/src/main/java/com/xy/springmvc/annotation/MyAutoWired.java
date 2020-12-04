package com.xy.springmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:自动注入注解
 * Version:V1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutoWired {
    String value() default "";
}
