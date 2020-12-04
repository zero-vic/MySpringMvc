package com.xy.springmvc.context;

import com.xy.springmvc.annotation.MyAutoWired;
import com.xy.springmvc.annotation.MyController;
import com.xy.springmvc.annotation.MyMapper;
import com.xy.springmvc.annotation.MyService;
import com.xy.springmvc.exception.ContextException;
import com.xy.springmvc.utils.XmlPaser;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:
 * Version:V1.0
 */
public class WebApplicationContext {
    //classpath:springmvc-config.xml
    String contextConfigLocation;
    //扫描到得className
    List<String> classNameList = new ArrayList<>();
    //IOC容器 装controller ， service 层 对象,dao 层对象
    Map<String, Object> iocMap = new ConcurrentHashMap<>();

    public Map<String, Object> getIocMap() {
        return this.iocMap;
    }

    public WebApplicationContext(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    /**
     * 解析springmvc-config.xml dom4j
     */
    public void refresh() {
        //basePackage springmvc-config.xml
        String basePackageStr = XmlPaser.getBasePackage(contextConfigLocation.split(":")[1]);
        String[] basePackages = basePackageStr.split(",");
        if (basePackages.length > 0) {
            for (String basePage : basePackages) {
                executeScanPackage(basePage);
            }
        }
//扫描之后得内容是[com.xy.meeting.controller.UserController.class,
// com.xy.meeting.service.impl.UserServiceImpl.class,
// com.xy.meeting.service.UserService.class]
//        System.out.println("扫描之后得内容是" + classNameList);
        executeInstance();
//        System.out.println("ioc容器内容：" + iocMap);

        //实现自动注入
        executeAutoWired();

    }


    /**
     * 扫描包
     *
     * @param basePage
     */
    private void executeScanPackage(String basePage) {
        URL url = this.getClass().getClassLoader().getResource("/" + basePage.replaceAll("\\.", "/"));
        String path = url.getFile();
        File dir = new File(path);

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                executeScanPackage(basePage + "." + f.getName());
            } else {
                String className = basePage + "." + f.getName().replaceAll(".class", "");

                classNameList.add(className);

            }
        }
    }

    /**
     * 实例化Spring容器中得对象
     */
    private void executeInstance() {
        if (classNameList.isEmpty()) {
            throw new ContextException("没有要实例化得class对象");
        }
        //遍历 classNameList 实例化对象 并存到 IOC容器中

        try {
            for (String className : classNameList) {
                //获取对象
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MyController.class)) {
                    //如果是MyController修饰得
                    //将key变成驼峰命名法 UserController -》 userController
                    String beanName = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
                    //存入map
                    iocMap.put(beanName, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(MyService.class)) {
                    //如果是MyService修饰得
                    //获取注解对象
                    MyService serviceAnnotation = clazz.getAnnotation(MyService.class);
                    //获取注解得value
                    String beanName = serviceAnnotation.value();
                    if ("".equals(beanName)) {
                        //默认值设为 UserServiceImpl -》 userService
                        //获取实现得接口
                        Class<?>[] interfaces = clazz.getInterfaces();
                        for (Class<?> c : interfaces) {
                            String beanName1 = c.getSimpleName().substring(0, 1).toLowerCase() + c.getSimpleName().substring(1);
                            iocMap.put(beanName1, clazz.newInstance());
                        }
                    } else {
                        //指定了值
                        iocMap.put(beanName, clazz.newInstance());
                    }

                } else if (clazz.isAnnotationPresent(MyMapper.class)) {
                    //如果是MyMapper修饰得
                    //获取注解对象
                    MyMapper mapperAnnotation = clazz.getAnnotation(MyMapper.class);
                    //获取注解得value
                    String beanName = mapperAnnotation.value();
                    if ("".equals(beanName)) {
                        //默认值设为 UserDaoImpl -》 userDao
                        //获取实现得接口
                        Class<?>[] interfaces = clazz.getInterfaces();
                        for (Class<?> c : interfaces) {
                            String beanName2 = c.getSimpleName().substring(0, 1).toLowerCase() + c.getSimpleName().substring(1);
                            iocMap.put(beanName2, clazz.newInstance());
                        }
                    } else {
                        //指定了值
                        iocMap.put(beanName, clazz.newInstance());
                    }

                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    /**
     * 实现AutoWired注解  自动注入
     */
    private void executeAutoWired() {
        try {
            if (iocMap.isEmpty()) {
                throw new ContextException("没有要实例化得class对象");
            }
            //遍历map
            for (Map.Entry<String, Object> entry : iocMap.entrySet()) {
                String key = entry.getKey();
                Object bean = entry.getValue();
                //拿到bean得属性
                Field[] declaredFields = bean.getClass().getDeclaredFields();
                //拿到有MyAutoWired修饰的属性
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(MyAutoWired.class)) {
                        //拿到注解对象
                        MyAutoWired autoWiredAnnotation = field.getAnnotation(MyAutoWired.class);
                        String beanName = autoWiredAnnotation.value();
                        if ("".equals(beanName)) {
                            Class<?> type = field.getType();
                            beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
                            //开启权限
                            field.setAccessible(true);
                            //属性注入
                            field.set(bean, iocMap.get(beanName));
                        } else {
                            //开启权限
                            field.setAccessible(true);
                            //属性注入
                            field.set(bean, iocMap.get(beanName));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
