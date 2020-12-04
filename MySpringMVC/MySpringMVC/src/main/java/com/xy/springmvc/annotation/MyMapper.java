package com.xy.springmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/24
 * Description:
 * Version:V1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyMapper {
    String value() default "";
}
