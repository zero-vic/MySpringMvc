package com.xy.meeting.dao.impl;

import com.xy.meeting.dao.UserDao;
import com.xy.meeting.pojo.PageBean;
import com.xy.meeting.pojo.User;
import com.xy.meeting.utils.JdbcUtil;
import com.xy.meeting.vo.UserStaffVo;
import com.xy.springmvc.annotation.MyMapper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.*;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/23
 * Description:
 * Version:V1.0
 */
@MyMapper
public class UserDaoImpl implements UserDao {
    private QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());

    @Override
    public User findUserByUsername(String userName) {
        User user = null;
        String sql = "SELECT * FROM t_user WHERE userName = ?";
        Object[] params = {userName};
        try {
            user = qr.query(sql, new BeanHandler<User>(User.class), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;

    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE t_user SET userName=?,userRealName=?,gender= ?,telPhone=?,lastLoginTime=? WHERE uid=?";
        Object[] params = {user.getUserName(), user.getUserRealName(), user.getGender(), user.getTelPhone(),
                user.getLastLoginTime(), user.getUId()};
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User findUserByUserid(Integer uid) {
        User user = null;
        String sql = "SELECT * FROM t_user WHERE uid = ?";
        Object[] params = {uid};
        try {
            user = qr.query(sql, new BeanHandler<User>(User.class), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void updatePassword(String password, Integer uid) {
        String sql = "UPDATE t_user SET password = ? WHERE uid = ?";
        Object[] params = {password, uid};
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 查询所有用户信息
     *
     * @return
     */
    @Override
    public List<User> findAll() {
        String sql = "select * from t_user";
        List<User> userList = null;

        try {
            userList = qr.query(sql, new BeanListHandler<User>(User.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public PageBean<User> findAll(Integer pageCode, Integer pageSize) {
        PageBean<User> pageBean = new PageBean<>();
        try {
            Integer totalRecords = getTotalRecords();
            List<User> datas = getDatas(pageCode, pageSize);
            pageBean.setPageSize(pageSize);
            pageBean.setPageCode(pageCode);
            pageBean.setDatas(datas);
            pageBean.setTotalRecords(totalRecords);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pageBean;
    }

    /**
     * 根据uid在t_user_staff表中查出staffid
     *
     * @param uId
     * @return
     */
    @Override
    public List<Integer> findStaffByUid(Integer uId) {
        String sql = "select staffId from t_user_staff where uId = ?";
        List<Integer> staffIds = null;
        try {
            staffIds = qr.query(sql, new ColumnListHandler<Integer>(), uId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return staffIds;
    }

    @Override
    public Integer addUser(UserStaffVo user) {
        Integer uId = null;
        String sql = "insert into t_user(userName,password,userRealName,gender,telPhone) values(?,?,?,?,?)";
        try {
            Object[] params = {user.getUserName(), user.getPassword(), user.getUserRealName(), user.getGender(), user.getTelPhone()};
            qr.update(JdbcUtil.getTranConnection(), sql, params);
            Number number = qr.query(JdbcUtil.getTranConnection(), "SELECT LAST_INSERT_ID()", new ScalarHandler<Integer>());
            uId = number.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uId;
    }

    @Override
    public void addUserToStaff(Integer uId, Integer staffId) {
        try {
            String sql = "insert into t_user_staff(uId,staffId) values(?,?)";
            Object[] params = {uId, staffId};
            qr.update(JdbcUtil.getTranConnection(), sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserToRoleByUid(Integer uid) {
        String sql = "delete from t_user_role where uId = ?";
        try {
            qr.update(JdbcUtil.getTranConnection(), sql, uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteUserToStaffByUid(Integer uid) {
        String sql = "delete from t_user_staff where uId = ?";
        try {
            qr.update(JdbcUtil.getTranConnection(), sql, uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserByUid(Integer uid) {
        String sql = "delete from t_user where uId = ?";
        try {
            qr.update(JdbcUtil.getTranConnection(), sql, uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUserByUid(Integer uid) {
        User user = null;
        String sql = "select * from t_user where uId = ?";
        try {
            user = qr.query(sql, new BeanHandler<>(User.class), uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public void updateUserAndStaff(UserStaffVo user) {
        String sql = "update t_user set userName = ?,password = ? ,userRealName = ?,gender = ?,telPhone = ? where uId = ? ";
        Object[] params = {user.getUserName(), user.getPassword(), user.getUserRealName(), user.getGender(), user.getTelPhone(), user.getUId()};
        try {
            qr.update(JdbcUtil.getTranConnection(), sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取总记录数
     *
     * @return
     */
    private Integer getTotalRecords() throws SQLException {
        String sql = "select COUNT(uId) from t_user";
        Number number = qr.query(sql, new ScalarHandler<>());
        Integer totalRecords = number.intValue();
        return totalRecords;
    }

    /**
     * 获取指定数据
     *
     * @param pageCode
     * @param pageSize
     * @return
     * @throws SQLException
     */
    private List<User> getDatas(Integer pageCode, Integer pageSize) throws SQLException {
        String sql = "select * from t_user limit ?,?";
        Object[] params = {(pageCode - 1) * pageSize, pageSize};
        List<User> datas = qr.query(sql, new BeanListHandler<User>(User.class), params);
        return datas;
    }

/**
 *
 *
 * String sql = "select  t1.* ,t3.staffName ,t4.delegationName from  t_user t1  " +
 *                 "LEFT JOIN  t_user_staff  t2  " +
 *                 "ON t1.uId = t2.uId   " +
 *                 "LEFT JOIN t_staff t3  " +
 *                 "ON t2.staffId = t3.staffId  " +
 *                 "LEFT JOIN t_delegation t4  " +
 *                 "ON t3.delegationId = t4.delegationId  ";
 *
 */

}
