package com.xy.meeting.dao.impl;

import com.xy.meeting.dao.RoleDao;
import com.xy.meeting.pojo.Role;
import com.xy.meeting.utils.JdbcUtil;
import com.xy.meeting.vo.RoleVo;
import com.xy.springmvc.annotation.MyMapper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/25
 * Description:
 * Version:V1.0
 */
@MyMapper
public class RoleDaoImpl implements RoleDao {
    private QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());

    @Override
    public List<Role> findAll() {
        String sql = "SELECT * FROM t_role";
        List<Role> roleList = null;
        try {
            roleList = qr.query(sql, new BeanListHandler<Role>(Role.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return roleList;
    }

    /**
     * 根据id删除role
     * roleid是permission的外键
     * 有点问题
     *
     * @param roleId
     */
    @Override
    public void deleteRoleByRoleId(Integer roleId) {
        String sql = "DELETE FROM t_role WHERE roleId = ?";
        try {
            qr.update(sql, roleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public int addRole(Role role) {
        Integer roleId = null;
        try {
            Connection connection = JdbcUtil.getTranConnection();
            String sql = "INSERT INTO t_role(roleName,roleDescription) values(?,?)";
            Object[] params = {role.getRoleName(), role.getRoleDescription()};

            qr.update(connection, sql, params);

            Number number = qr.query(connection, "SELECT LAST_INSERT_ID()", new ScalarHandler<>());
            roleId = number.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roleId;
    }

    @Override
    public void addRoleToPermission(Integer roleId, int perId) {
        try {
            Connection connection = JdbcUtil.getTranConnection();
            String sql = "insert into t_role_perm VALUES(?,?)";
            Object[] params = {roleId, perId};

            qr.update(connection, sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Role findByRoleName(Role role) {
        String sql = "SELECT * FROM t_role WHERE roleName = ? ";
        Role role1 = null;
        try {
            role1 = qr.query(sql, new BeanHandler<Role>(Role.class), role.getRoleName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return role1;
    }

    @Override
    public void deleteRoleToPermissionByRoleId(Integer roleId) {
        String sql = "delete from t_role_perm where roleId = ?";
        try {
            qr.update(JdbcUtil.getTranConnection(), sql, roleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Role findRoleById(Integer roleId) {
        String sql = "select * from t_role where roleId = ?";
        Role role = null;
        try {
            role = qr.query(sql, new BeanHandler<Role>(Role.class), roleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return role;
    }

    @Override
    public List<Integer> findPerIdByRoleId(Integer roleId) {
        String sql = "select perId from t_role_perm where roleId = ?";
        List<Integer> perIds = null;
        try {
            Connection connection = JdbcUtil.getTranConnection();
            perIds = qr.query(connection, sql, new ColumnListHandler<Integer>(), roleId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return perIds;
    }

    @Override
    public void updateRole(RoleVo roleVo) {
        try {

            Connection connection = JdbcUtil.getTranConnection();
            String sql = "update t_role set roleName = ? ,roleDescription = ? where roleId = ?";
            Object[] params = {roleVo.getRoleName(), roleVo.getRoleDescription(), roleVo.getRoleDescription()};
            qr.update(connection, sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
