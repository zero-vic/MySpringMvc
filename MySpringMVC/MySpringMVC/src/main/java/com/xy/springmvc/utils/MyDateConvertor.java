package com.xy.springmvc.utils;

import org.apache.commons.beanutils.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateConvertor implements Converter {
    /**
     *
     * @param aClass
     * @param o 表示需要被转换的值，在servlet中也就是从前端获取到的值
     * @param
     * @return
     */
    public Object convert(Class aClass, Object o) {
        /*
        如果被转换的值为null,则直接返回null
         */
        if(o == null){
            return null;
        }
        /*
        如果被转换的值不是String类型，则直接返回该值
         */
        if(! (o instanceof String)){
            return o;
        }

        //代码能执行到这里，说明被转换的值不是Null,而且一定是String类型
        String obj = (String)o;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(obj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
