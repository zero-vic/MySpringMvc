package com.xy.meeting.dao;

import com.xy.meeting.pojo.Permission;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/26
 * Description:
 * Version:V1.0
 */
public interface PermissionDao {
    List<Permission> findAll();

    void deletePermissionByPerId(Integer perId);

    void deleteRoleToPermissionByPerId(Integer perId);

    void addPermission(Permission permission);

    Permission findPermissionByPerId(Integer perId);

    void updatePermission(Permission permission);

    Permission findPermByPerId(Integer perId);
}
