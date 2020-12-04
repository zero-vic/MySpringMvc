package com.xy.springmvc.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xy.springmvc.annotation.*;
import com.xy.springmvc.context.WebApplicationContext;
import com.xy.springmvc.exception.ContextException;
import com.xy.springmvc.handler.MyHandler;
import com.xy.springmvc.utils.JavaTypeUtil;
import com.xy.springmvc.utils.MyDateConvertor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:
 * Version:V1.0
 */
public class DispatcherServlet extends HttpServlet {
    private WebApplicationContext webApplicationContext;

    List<MyHandler> handlerList = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        //获取contextConfigLocation
        String contextConfigLocation = this.getInitParameter("contextConfigLocation");
        //获取spring容器
        webApplicationContext = new WebApplicationContext(contextConfigLocation);
        //解析springmvc-config.xml 文件 dom4j解析
        //初始化spring容器
        webApplicationContext.refresh();

        //初始化请求映射
        initHandlerMapping();
//        System.out.println(handlerList);

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        executeDispatcher(req, resp);

    }


    /**
     * 初始化请求映射
     */
    private void initHandlerMapping() {
        Map<String, Object> iocMap = webApplicationContext.getIocMap();
        if (iocMap.isEmpty()) {
            throw new ContextException("没有初始化的bean对象");
        }
        //遍历map
        for (Map.Entry<String, Object> entry : iocMap.entrySet()) {
            //获取对象
            Class<?> clazz = entry.getValue().getClass();
            //找到controller
            if (clazz.isAnnotationPresent(MyController.class)) {
                //拿到 controller 下的所有方法
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method method : declaredMethods) {
                    //找到RequestMapping修饰的方法
                    if (method.isAnnotationPresent(MyRequestMapping.class)) {
                        MyRequestMapping myRequestMappingAnnotation = method.getAnnotation(MyRequestMapping.class);
                        //拿到请求地址
                        String url = myRequestMappingAnnotation.value();
                        MyHandler handler = new MyHandler(url, entry.getValue(), method);
                        handlerList.add(handler);

                    }
                }
            }
        }
    }

    /**
     * 处理请求
     *
     * @param req
     * @param resp
     */
    private void executeDispatcher(HttpServletRequest req, HttpServletResponse resp) {
        MyHandler handler = getHandler(req);
        try {
            if (handler == null) {
                resp.getWriter().write("<h1>404 NOT FOUND!</h1>");
            } else {
                /**
                 //获取方法参数类型
                 Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
                 //请求参数数组
                 Object[] params = new Object[parameterTypes.length];

                 for (int i = 0; i < parameterTypes.length; i++) {
                 Class<?> parameterType = parameterTypes[i];
                 if ("HttpServletRequest".equals(parameterType.getSimpleName())) {
                 params[i] = req;
                 } else if ("HttpServletResponse".equals(parameterType.getSimpleName())) {
                 params[i] = resp;
                 }
                 }

                 //获取请求参数的集合
                 Map<String, String[]> parameterMap = req.getParameterMap();
                 for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                 String name = entry.getKey();
                 String value = entry.getValue()[0];
                 int index = hasRequestParam(handler.getMethod(), name);
                 if (index != -1) {
                 params[index] = value;
                 } else {
                 List<String> nameList = getParameterName(handler.getMethod());
                 for (int i = 0; i < nameList.size(); i++) {
                 if (name.equals(nameList.get(i))) {
                 params[i] = value;
                 break;
                 }
                 }
                 }
                 }
                 */
                //获取参数
                Object[] params = handlerParameters(req, resp, handler.getMethod());
                //调用控制层的方法
                Object result = handler.getMethod().invoke(handler.getController(), params);
                if (result instanceof String) {
                    String viewName = (String) result;
                    if (viewName.contains(":")) {
                        //转发 forward
                        String viewType = viewName.split(":")[0];
                        String viewPage = viewName.split(":")[1];
                        if (viewType.equals("forward")) {
//                            请求转发
                            req.getRequestDispatcher(viewPage).forward(req, resp);
                        } else if (viewType.equals("redirect")) {
//                            重定向
                            resp.sendRedirect(viewPage);
                        }
                    } else {
                        //默认转发
                        req.getRequestDispatcher(viewName).forward(req, resp);
                    }
                } else {
                    //返回JSON
                    Method method = handler.getMethod();
                    if (method.isAnnotationPresent(MyResponseBody.class)) {
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        String json = objectMapper.writeValueAsString(result);
//                        resp.setCharacterEncoding("UTF-8");
//                        PrintWriter out = resp.getWriter();
//                        out.write(json);
//                        out.flush();
//                        out.close();
                        sendResponse(result,resp);
                    }
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析请求 获取 handler
     *
     * @param req
     * @return
     */
    private MyHandler getHandler(HttpServletRequest req) {
        String uri = req.getRequestURI();
        for (MyHandler handler : handlerList) {
            if (handler.getUrl().equals(uri)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * 判断控制器方法的参数 是否有 MyRequestParam注解 找到对应的value 找到返回下标 没找到返回-1
     *
     * @param method
     * @param name
     * @return
     */
    private int hasRequestParam(Method method, String name) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(MyRequestParam.class)) {
                MyRequestParam parameterAnnotation = parameter.getAnnotation(MyRequestParam.class);
                String requestParamValue = parameterAnnotation.value();
                if (name.equals(requestParamValue)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 获取方法参数的列表
     *
     * @param method
     * @return
     */
    private List<String> getParameterName(Method method) {
        List<String> list = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            list.add(parameter.getName());
        }
        return list;
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @param response
     * @param method
     * @return
     */
    private Object[] handlerParameters(HttpServletRequest request, HttpServletResponse response, Method method) throws IOException {
        //获取方法参数
        Parameter[] parameters = method.getParameters();
        //获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        //创建存参数的值的数组
        Object[] params = new Object[parameters.length];
        //遍历，将参数的值装入params数组
        for (int i = 0; i < parameters.length; i++) {
            //获取当前参数的名字
            String parameterName = parameters[i].getName();
            //获取当前参数的类型
            Class parameterType = parameterTypes[i];
            //判断是否是HttpServletRequest类型的参数
            if (parameterType == HttpServletRequest.class) {
                params[i] = request;
                continue;
            }
            //判断是否是HttpServletResponse类型的参数
            if (parameterType == HttpServletResponse.class) {
                params[i] = response;
                continue;
            }
            boolean isSimpleType = judgeIsSimpleType(parameterType);
            //如果当前参数是简单类型的
            if (isSimpleType) {
                //根据参数名获取参数的value
                String value = request.getParameter(parameterName);
                //每种类型参数为空的时候默认值可能不同，所以要判断
                if (parameterType == String.class) {
                    params[i] = value;
                } else if (parameterType == int.class || parameterType == Integer.class) {
                    if (value == null) {
                        params[i] = 0;
                    } else {
                        params[i] = Integer.parseInt(value);
                    }
                } else if (parameterType == double.class || parameterType == Double.class) {
                    if (value == null) {
                        params[i] = 0.0;
                    } else {
                        params[i] = Double.parseDouble(value);
                    }
                } else if (parameterType == float.class || parameterType == Float.class) {
                    if (value == null) {
                        params[i] = 0.0;
                    } else {
                        params[i] = Float.parseFloat(value);
                    }
                }
            } else {
                //当前参数背MyRequestBody注解修饰 前端给定参数是 json格式
                if (parameters[i].isAnnotationPresent(MyRequestBody.class)) {
                    String jsonstr = getJsonStr(request);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Object obj = objectMapper.readValue(jsonstr, parameterType);
                    params[i] = obj;

                } else {
                    //如果不是普通类型的参数，如对象，json 格式时处理
                    Object instance = null;
                    try {
                        //如果是对象时（User）
                        instance = parameterType.newInstance();
                        ConvertUtils.register(new MyDateConvertor(), Date.class);
                        BeanUtils.populate(instance, request.getParameterMap());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    params[i] = instance;
                }
            }
        }
        return params;
    }

    /**
     * 判断该参数类型是否是简单类型(int、integer、double。。。)
     *
     * @param paramType
     * @return
     */
    private boolean judgeIsSimpleType(Class paramType) {
        Class[] javaSimpleTypes = JavaTypeUtil.getJavaSimpleTypes();
        for (Class clazz : javaSimpleTypes) {
            if (paramType == clazz) {
                return true;
            }
        }
        return false;
    }

    private String getJsonStr(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        String jsonStr = new String(bytes, "UTF-8");
        return jsonStr;
    }
    /**
     * 向前端发送JSON格式的字符串作为响应
     * @param result
     * @param resp
     * @throws IOException
     */
    private void sendResponse(Object result, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getOutputStream(), result);
    }


}
