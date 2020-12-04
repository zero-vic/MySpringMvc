package com.xy.meeting.dao.impl;

import com.xy.meeting.dao.PermissionDao;
import com.xy.meeting.pojo.Permission;
import com.xy.meeting.utils.JdbcUtil;
import com.xy.springmvc.annotation.MyMapper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/26
 * Description:
 * Version:V1.0
 */
@MyMapper
public class PermissionDaoImpl implements PermissionDao {
    private QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());


    @Override
    public List<Permission> findAll() {
        String sql = "SELECT * FROM t_perm";
        List<Permission> permissionList = null;
        try {
            permissionList = qr.query(sql, new BeanListHandler<Permission>(Permission.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return permissionList;
    }

    @Override
    public void deletePermissionByPerId(Integer perId) {
        String sql = "delete from t_perm where perId = ?";
        try {
            qr.update(sql, perId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteRoleToPermissionByPerId(Integer perId) {
        String sql = "delete from t_role_perm where perId = ?";
        try {
            qr.update(sql, perId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPermission(Permission permission) {
        String sql = "insert into t_perm values(NULL,?,?) ";
        Object[] params = {permission.getPerName(), permission.getPerDes()};
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Permission findPermissionByPerId(Integer perId) {
        String sql = "select * from t_perm where perId = ?";
        Permission permission = null;
        try {
            permission = qr.query(sql, new BeanHandler<Permission>(Permission.class), perId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return permission;
    }

    @Override
    public void updatePermission(Permission permission) {
        String sql = "update t_perm set perName = ?,perDes = ? where perId = ?";
        Object[] params = {permission.getPerName(), permission.getPerDes(), permission.getPerId()};
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Permission findPermByPerId(Integer perId) {
        Permission permission = null;
        String sql = "select * from t_perm where perId = ? ";
        try {
            permission = qr.query(sql, new BeanHandler<Permission>(Permission.class), perId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return permission;
    }
}
