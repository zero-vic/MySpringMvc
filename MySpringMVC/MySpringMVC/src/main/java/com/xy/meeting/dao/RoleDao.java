package com.xy.meeting.dao;

import com.xy.meeting.pojo.Role;
import com.xy.meeting.vo.RoleVo;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/25
 * Description:
 * Version:V1.0
 */
public interface RoleDao {
    List<Role> findAll() throws Exception;

    void deleteRoleByRoleId(Integer roleId) throws Exception;

    int addRole(Role role) throws Exception;

    void addRoleToPermission(Integer roleId, int perId) throws Exception;

    Role findByRoleName(Role role) throws Exception;

    void deleteRoleToPermissionByRoleId(Integer roleId) throws Exception;

    Role findRoleById(Integer roleId);

    List<Integer> findPerIdByRoleId(Integer roleId);

    void updateRole(RoleVo roleVo);
}
