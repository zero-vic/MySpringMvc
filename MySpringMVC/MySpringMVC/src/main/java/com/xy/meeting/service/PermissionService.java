package com.xy.meeting.service;

import com.xy.meeting.pojo.Permission;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/26
 * Description:
 * Version:V1.0
 */
public interface PermissionService {

   List<Permission> findAll() throws Exception;

    void deletePermissionByPerId(Integer perId) throws Exception;

    void addPermission(Permission permission) throws Exception;

    void updatePermission(Permission permission) throws  Exception;

    Permission findPermByPerId(Integer perId) throws  Exception;
}
