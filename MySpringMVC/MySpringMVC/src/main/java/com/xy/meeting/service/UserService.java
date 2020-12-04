package com.xy.meeting.service;

import com.xy.meeting.exception.BussinessException;
import com.xy.meeting.pojo.PageBean;
import com.xy.meeting.pojo.User;
import com.xy.meeting.vo.UserStaffVo;
import com.xy.meeting.vo.UserVo;
import com.xy.springmvc.annotation.MyService;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:
 * Version:V1.0
 */

public interface UserService {
    User login(UserVo userVo, String text) throws BussinessException;

    void sendMail() throws Exception;

    User findUserByUsername(String userName) throws Exception;

    void updateUser(User user);

    User findUserByUserid(Integer uid);

    void updatePassword(String password, Integer uid);

    List<User> findAll() throws Exception;

    PageBean<User> findAll(Integer pageCode, Integer pageSize) throws Exception;

    void addUser(UserStaffVo userStaffVo) throws Exception;

    void deleteUserByUid(Integer uid) throws Exception;

    User findUserByUid(Integer uid) throws Exception;

    void updateUserAndStaff(UserStaffVo userStaffVo) throws Exception;
}
