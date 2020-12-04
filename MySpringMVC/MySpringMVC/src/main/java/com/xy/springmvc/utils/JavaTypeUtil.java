package com.xy.springmvc.utils;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/23
 * Description:
 * Version:V1.0
 */
public class JavaTypeUtil {
    private static Class[] javaSimpleDataTypes={
      String.class, Integer.class,Double.class,Float.class,
      int.class, char.class,double.class,float.class
    };
    public static Class[] getJavaSimpleTypes(){
        return javaSimpleDataTypes;
    }
}
