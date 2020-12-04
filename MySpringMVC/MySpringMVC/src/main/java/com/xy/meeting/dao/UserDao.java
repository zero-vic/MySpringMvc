package com.xy.meeting.dao;

import com.xy.meeting.pojo.PageBean;
import com.xy.meeting.pojo.User;
import com.xy.meeting.vo.UserStaffVo;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/23
 * Description:
 * Version:V1.0
 */
public interface UserDao {
    User findUserByUsername(String userName);

    void updateUser(User user);

    User findUserByUserid(Integer uid);

    void updatePassword(String password, Integer uid);

    List<User> findAll();

    PageBean<User> findAll(Integer pageCode,Integer pageSize);

    List<Integer> findStaffByUid(Integer uId);

    Integer addUser(UserStaffVo user);

    void addUserToStaff(Integer uId, Integer staffId);

    void deleteUserToRoleByUid(Integer uid);

    void deleteUserToStaffByUid(Integer uid);

    void deleteUserByUid(Integer uid);

    User findUserByUid(Integer uid);

    void updateUserAndStaff(UserStaffVo userStaffVo);
}
