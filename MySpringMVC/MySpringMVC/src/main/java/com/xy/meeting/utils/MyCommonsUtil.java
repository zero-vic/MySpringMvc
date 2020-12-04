package com.xy.meeting.utils;

import com.xy.springmvc.utils.MyDateConvertor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

public class MyCommonsUtil {

    /*
    创建对象
    注册日期转换器
    装配属性
     */
    public static <T> T processBean(Class<T> clazz, Map map){
        T t = null;
        try {
            t = clazz.newInstance();
            ConvertUtils.register(new MyDateConvertor(), Date.class);
            BeanUtils.populate(t, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return t;
    }
}
