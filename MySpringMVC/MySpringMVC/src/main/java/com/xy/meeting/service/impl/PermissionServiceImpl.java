package com.xy.meeting.service.impl;

import com.xy.meeting.dao.PermissionDao;
import com.xy.meeting.pojo.Permission;
import com.xy.meeting.service.PermissionService;
import com.xy.springmvc.annotation.MyAutoWired;
import com.xy.springmvc.annotation.MyService;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/26
 * Description:
 * Version:V1.0
 */
@MyService
public class PermissionServiceImpl implements PermissionService {
    @MyAutoWired
    PermissionDao permissionDao;


    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

    @Override
    public void deletePermissionByPerId(Integer perId) throws Exception {
        permissionDao.deleteRoleToPermissionByPerId(perId);
        permissionDao.deletePermissionByPerId(perId);

    }

    @Override
    public void addPermission(Permission permission) throws Exception {
        permissionDao.addPermission(permission);
    }

    @Override
    public void updatePermission(Permission permission) {
        permissionDao.updatePermission(permission);
    }

    @Override
    public Permission findPermByPerId(Integer perId) {
        return permissionDao.findPermByPerId(perId);
    }
}
