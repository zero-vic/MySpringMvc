package com.xy.meeting.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/25
 * Description:过滤器，判断用户访问指定资源时，是否登录
 * Version:V1.0
 */
public class LoginFilter implements Filter {

    String[] staticResource = {".jsp", ".png", ".ico", ".js", ".css", "login.html", ".jpg", ".jpeg"};
    String[] dynamicResource = {"/users/verify", "/users/login"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String requestURI = req.getRequestURI();
        //判断是否访问的是可放行的静态资源
        boolean isStaticResource = judgeStaticResource(requestURI);
        if(isStaticResource){
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        //判断是否访问的是可放行的动态资源
        boolean isDynamicResource = judgeDynamicResource(requestURI);
        if(isDynamicResource){
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        //判断用户是否登录，如果登录了，也放行
        boolean isLogin = judgeLogin(req);
        if(isLogin){
            chain.doFilter(servletRequest, servletResponse);
        }else{
            //如果没有登录，跳转到登录界面
            resp.sendRedirect("/html/login.html");
        }

    }
    /**
     * 判断用户是否登录
     * @param req
     * @return
     */
    private boolean judgeLogin(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("loginUser")){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断请求的资源是否输入被放行的动态资源，如果是，返回true
     * @param requestURI
     * @return
     */
    private boolean judgeDynamicResource(String requestURI) {
        for(String resource : dynamicResource){
            if(requestURI.endsWith(resource)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断访问的资源是否是静态资源，如果是，则返回true
     * @param requestURI
     * @return
     */
    private boolean judgeStaticResource(String requestURI) {
        for(String resource : staticResource){
            if(requestURI.endsWith(resource)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
