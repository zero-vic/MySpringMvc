package com.xy.springmvc.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/22
 * Description:
 * Version:V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyHandler {
    private String url;
    private Object controller;
    private Method method;
}
