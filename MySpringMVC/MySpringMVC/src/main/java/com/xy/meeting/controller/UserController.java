package com.xy.meeting.controller;

import com.xy.meeting.exception.BussinessException;
import com.xy.meeting.pojo.PageBean;
import com.xy.meeting.pojo.User;
import com.xy.meeting.response.ResponseCode;
import com.xy.meeting.service.UserService;
import com.xy.meeting.utils.ExceptionUtil;
import com.xy.meeting.utils.VerifyCode;
import com.xy.meeting.vo.UserStaffVo;
import com.xy.meeting.vo.UserVo;
import com.xy.springmvc.annotation.*;
import com.xy.springmvc.response.ModelAndView;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:
 * Version:V1.0
 */
@MyController
public class UserController {
    @MyAutoWired
    UserService userService;

    /**
     * 验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @MyRequestMapping("/users/verify")
    public void verify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        VerifyCode vc = new VerifyCode();
        BufferedImage image = vc.getImage();
        String text = vc.getText();
        request.getSession().setAttribute("vc", text);
        VerifyCode.output(image, response.getOutputStream());
    }

    /**
     * 登录请求
     *
     * @param userVo
     * @param request
     * @param response
     * @return
     */
    @MyRequestMapping("/users/login")
    @MyResponseBody
    public ModelAndView<User> login(@MyRequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        String text = (String) request.getSession().getAttribute("vc");
        try {
            User user = userService.login(userVo, text);
            modelAndView.setRc(ResponseCode.SUCCESS);
            modelAndView.setView("/html/index.html");
            modelAndView.setObj(user);
        } catch (Exception e) {
            if (e instanceof BussinessException) {
                BussinessException be = (BussinessException) e;
                for (ResponseCode rc : ResponseCode.values()) {
                    if (be.getCode() == rc.getCode()) {
                        modelAndView.setRc(rc);
                    }
                }
            } else if (e instanceof SQLException) {
                modelAndView.setRc(ResponseCode.SQLEXCEPTION);
            } else {
                modelAndView.setRc(ResponseCode.SYSTEMEXCEPTION);
            }
        }
        return modelAndView;
    }

    /**
     * 退出登录
     * 清除cookie
     *
     * @return
     */
    @MyRequestMapping("/users/logout")
    @MyResponseBody
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        Cookie[] cookies = request.getCookies();
        boolean flag = true;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("loginUser")) {
                flag = false;
                //删除cookie
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                modelAndView.setRc(ResponseCode.SUCCESS);
                modelAndView.setView("/html/login.html");
            }
        }
        if (flag) {
            modelAndView.setRc(ResponseCode.SQLEXCEPTION);
        }
        return modelAndView;
    }

    /**
     * 给运维发邮件
     *
     * @return
     */
    @MyRequestMapping("/users/sendMail")
    @MyResponseBody
    public ModelAndView sendMail() {
        ModelAndView mv = new ModelAndView();
        try {
            userService.sendMail();
            mv.setRc(ResponseCode.SUCCESS);
        } catch (Exception e) {
            mv.setRc(ResponseCode.MAILEXCEPTION);
        }
        return mv;
    }

    /**
     * 根据用户名查询用户
     */
    @MyRequestMapping("/users/findUserByUserid")
    @MyResponseBody
    public ModelAndView<User> findUserByUserid(Integer uid) {
        ModelAndView<User> mv = new ModelAndView<>();
        try {
            User user = userService.findUserByUserid(uid);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setObj(user);
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }

        return mv;
    }

    /**
     * 根据id更新信息
     *
     * @param user
     * @return
     */
    @MyRequestMapping("/users/updateUser")
    @MyResponseBody
    public ModelAndView updateUser(@MyRequestBody User user) {
        ModelAndView mv = new ModelAndView();

        try {
            userService.updateUser(user);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/index.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }

        return mv;
    }

    /**
     * 根据password 和uid 验证密码
     *
     * @param password
     * @param uid
     * @return
     */
    @MyRequestMapping("/users/verifyPassword")
    @MyResponseBody
    public ModelAndView<User> verifyPassword(String password, Integer uid) {
        ModelAndView mv = new ModelAndView();
        try {
            System.out.println("password " + password + "uid" + uid);
            User user = userService.findUserByUserid(uid);
            System.out.println(user);
            if (password.equals(user.getPassword())) {
                mv.setRc(ResponseCode.SUCCESS);
            } else {
                mv.setRc(ResponseCode.PASSWORDEXCEPTION);
            }
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

    /**
     * 根据uid更新用户的密码
     *
     * @param password
     * @param uid
     * @return
     */
    @MyRequestMapping("/users/updatePassword")
    @MyResponseBody
    public ModelAndView updatePassword(String password, Integer uid) {
        ModelAndView mv = new ModelAndView();
        try {
            userService.updatePassword(password, uid);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/login.html");

        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }


        return mv;
    }

    /**
     * 查询所有用户，包括他们的staff，delegation
     *
     * @return
     */
//不带分页
//    @MyRequestMapping("/users/findAll")
//    @MyResponseBody
//    public ModelAndView<User> findAll() {
//        ModelAndView mv = new ModelAndView();
//        try {
//            List<User> userList = userService.findAll();
//            mv.setRc(ResponseCode.SUCCESS);
//            mv.setObj(userList);
//        } catch (Exception e) {
//            ExceptionUtil.sqlExceptionChoice(mv, e);
//        }
//        return mv;
//    }
    @MyRequestMapping("/users/findAll")
    @MyResponseBody
    public ModelAndView findAll(Integer pageCode) {
        ModelAndView mv = new ModelAndView();
        try {
            Integer pageSize = 10;
            PageBean<User> pageBean = userService.findAll(pageCode, pageSize);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setObj(pageBean);
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

    /**
     * 添加用户
     *
     * @param
     * @param
     * @return
     */
    @MyRequestMapping("/users/addUser")
    @MyResponseBody
    public ModelAndView addUser(@MyRequestBody UserStaffVo userStaffVo) {
        ModelAndView mv = new ModelAndView();
        try {

            userService.addUser(userStaffVo);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/userList.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

    /**
     * 根据uid删除User
     *
     * @param uid
     * @return
     */
    @MyRequestMapping("/users/deleteUserByUid")
    @MyResponseBody
    public ModelAndView deleteUserByUid(Integer uid) {
        ModelAndView mv = new ModelAndView();
        try {
            userService.deleteUserByUid(uid);
            mv.setRc(ResponseCode.SUCCESS);
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

    /**
     * 根据id查询 user staff
     *
     * @param uid
     * @return
     */
    @MyRequestMapping("/users/findUserByUid")
    @MyResponseBody
    public ModelAndView<User> findUserByUid(Integer uid) {
        ModelAndView<User> mv = new ModelAndView<>();
        try {
            User user = userService.findUserByUid(uid);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setObj(user);
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

    /**
     * 根据id更新user 和其职业
     *
     * @param userStaffVo
     * @return
     */
    @MyRequestMapping("/users/updateUserAndStaff")
    @MyResponseBody
    public ModelAndView updateUserAndStaff(@MyRequestBody UserStaffVo userStaffVo) {
        ModelAndView mv = new ModelAndView();
        try {
            userService.updateUserAndStaff(userStaffVo);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/userListNew.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;

    }

}
