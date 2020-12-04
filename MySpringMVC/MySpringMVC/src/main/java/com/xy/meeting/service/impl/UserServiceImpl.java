package com.xy.meeting.service.impl;

import com.xy.meeting.dao.StaffDao;
import com.xy.meeting.dao.UserDao;
import com.xy.meeting.dao.impl.UserDaoImpl;
import com.xy.meeting.exception.BussinessException;
import com.xy.meeting.pojo.PageBean;
import com.xy.meeting.pojo.Staff;
import com.xy.meeting.pojo.User;
import com.xy.meeting.response.ResponseCode;
import com.xy.meeting.service.UserService;
import com.xy.meeting.utils.JdbcUtil;
import com.xy.meeting.utils.MD5Util;
import com.xy.meeting.utils.MailUtil;
import com.xy.meeting.vo.UserStaffVo;
import com.xy.meeting.vo.UserVo;
import com.xy.springmvc.annotation.MyAutoWired;
import com.xy.springmvc.annotation.MyService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:
 * Version:V1.0
 */
@MyService
public class UserServiceImpl implements UserService {
    @MyAutoWired
    UserDao userDao;
    @MyAutoWired
    StaffDao staffDao;

    @Override
    public User login(UserVo userVo, String text) throws BussinessException {
        String code = userVo.getCode();
        if (!text.toUpperCase().equals(code.toUpperCase())) {
            throw new BussinessException(ResponseCode.VERIFYEXCEPTION);
        }
        User user = userDao.findUserByUsername(userVo.getUserName());
        if (user == null) {
            throw new BussinessException(ResponseCode.USERNAMEEXCEPTION);
        }
        if (!user.getPassword().equals(userVo.getPassword())) {
            throw new BussinessException(ResponseCode.PASSWORDEXCEPTION);
        }
        String password = user.getPassword();
        String encrypt = MD5Util.encrypt(password);
        user.setPassword(encrypt);
        return user;

    }

    @Override
    public void sendMail() throws Exception {
        String toUser = "1623272666@qq.com";
        String content = "运维你好，请到我这里来喝茶";
        String title = "meeting系统呼叫运维";
        try {
            MailUtil.sendMail(toUser, content, title);
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    @Override
    public User findUserByUsername(String userName) {
        return userDao.findUserByUsername(userName);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public User findUserByUserid(Integer uid) {
        return userDao.findUserByUserid(uid);
    }

    @Override
    public void updatePassword(String password, Integer uid) {
        userDao.updatePassword(password, uid);
    }

    @Override
    public List<User> findAll() throws Exception {
        List<User> userList = userDao.findAll();
        for (User user : userList) {
            Integer uId = user.getUId();
            List<Integer> staffIds = userDao.findStaffByUid(uId);
            for (Integer staffId : staffIds) {
                Staff staff = staffDao.findByStaffId(staffId);
                user.getStaffList().add(staff);
            }
        }
        return userList;
    }

    public PageBean<User> findAll(Integer pageCode, Integer pageSize) {
        PageBean<User> pageBean = userDao.findAll(pageCode, pageSize);
        List<User> userList = pageBean.getDatas();
        for (User user : userList) {
            Integer uId = user.getUId();
            List<Integer> staffIds = userDao.findStaffByUid(uId);
            for (Integer staffId : staffIds) {
                Staff staff = staffDao.findByStaffId(staffId);
                user.getStaffList().add(staff);
            }
        }

        return pageBean;
    }

    @Override
    public void addUser(UserStaffVo userStaffVo) throws SQLException {
        //开启事务
        JdbcUtil.beginTransaction();
        try {
            //添加User表
            Integer uId = userDao.addUser(userStaffVo);
//        添加t_user_staff表
            List<Integer> staffIds = userStaffVo.getStaffIds();
            for (Integer staffId : staffIds) {
                userDao.addUserToStaff(uId, staffId);
            }
            JdbcUtil.commit();
        } catch (Exception e) {
            //回滚
            JdbcUtil.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserByUid(Integer uid) throws Exception {
        //开启事务
        JdbcUtil.beginTransaction();
        try {

            //删除t_user_role 表 中数据
            userDao.deleteUserToRoleByUid(uid);
            //删除t_user_staff 表中数据
            userDao.deleteUserToStaffByUid(uid);
            //删除t_user 表数据
            userDao.deleteUserByUid(uid);

            //提交事务
            JdbcUtil.commit();
        } catch (SQLException e) {
            //回滚s
            JdbcUtil.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUserByUid(Integer uid) throws Exception {
        User user = userDao.findUserByUid(uid);
        List<Integer> staffIds = userDao.findStaffByUid(uid);
        for (Integer staffId : staffIds) {
            Staff staff = staffDao.findByStaffId(staffId);
            user.getStaffList().add(staff);
        }
        return user;
    }

    @Override
    public void updateUserAndStaff(UserStaffVo userStaffVo) throws Exception {
        //开启事务
        JdbcUtil.beginTransaction();
        try{
            //更新 user表
            userDao.updateUserAndStaff(userStaffVo);

            //删除 t_user_staff 表的记录
            userDao.deleteUserToStaffByUid(userStaffVo.getUId());

            //再t_user_staff 添加新的记录
            List<Integer> staffIds = userStaffVo.getStaffIds();
            for(Integer staffId : staffIds){
                userDao.addUserToStaff(userStaffVo.getUId(),staffId);
            }
            //提交
            JdbcUtil.commit();
        }catch (SQLException e){
            //回滚
            JdbcUtil.rollback();
            throw new RuntimeException(e);
        }


    }
}
