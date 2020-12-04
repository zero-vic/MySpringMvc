package com.xy.meeting.test;

import com.xy.springmvc.utils.XmlPaser;
import org.junit.Test;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:
 * Version:V1.0
 */
public class TestSpringMvc {
    @Test
    public void testReadXml(){
        String basePackage = XmlPaser.getBasePackage("springmvc-config.xml");
        String simpleName = this.getClass().getSimpleName();
        String str = "com.xy.meeting.controller,com.xy.meeting.service.class";
        System.out.println("---"+str.replaceAll(".class", ""));
        String s = str.replaceAll(".class", "");
        System.out.println(s);
        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.xy.meeting.controller.UserController");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(clazz.getSimpleName());
        System.out.println(simpleName);
        System.out.println(basePackage);
    }
}
