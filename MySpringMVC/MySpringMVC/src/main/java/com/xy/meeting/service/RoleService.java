package com.xy.meeting.service;

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
public interface RoleService {
    List<Role> findAll() throws Exception;

    void deleteRoleByRoleId(Integer roleId) throws Exception;

    void addRole(RoleVo roleVo) throws Exception;

    Role findRoleById(Integer roleId) throws Exception;

    void updateRole(RoleVo roleVo) throws Exception;
}
