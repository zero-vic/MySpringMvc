package com.xy.meeting.service.impl;

import com.xy.meeting.dao.PermissionDao;
import com.xy.meeting.dao.RoleDao;
import com.xy.meeting.pojo.Permission;
import com.xy.meeting.pojo.Role;
import com.xy.meeting.service.RoleService;
import com.xy.meeting.utils.JdbcUtil;
import com.xy.meeting.vo.RoleVo;
import com.xy.springmvc.annotation.MyAutoWired;
import com.xy.springmvc.annotation.MyService;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/25
 * Description:
 * Version:V1.0
 */
@MyService
public class RoleServiceImpl implements RoleService {
    @MyAutoWired
    RoleDao roleDao;
    @MyAutoWired
    PermissionDao permissionDao;

    @Override
    public List<Role> findAll() throws Exception {
        return roleDao.findAll();
    }

    @Override
    public void deleteRoleByRoleId(Integer roleId) throws Exception {
        roleDao.deleteRoleToPermissionByRoleId(roleId);
        roleDao.deleteRoleByRoleId(roleId);
    }

    @Override
    public void addRole(RoleVo roleVo) throws Exception {
        //开启事务
        JdbcUtil.beginTransaction();
        try {
            Role role = new Role();
            role.setRoleName(roleVo.getRoleName());
            role.setRoleDescription(roleVo.getRoleDescription());
            Integer roleId = roleDao.addRole(role);
            addRoleToPermission(roleId, roleVo.getPerId());
            //提交事务
            JdbcUtil.commit();
        } catch (Exception e) {
            //回滚事务
            JdbcUtil.rollback();
            throw new Exception(e);
        }


    }

    @Override
    public Role findRoleById(Integer roleId) throws Exception {
        Role role = roleDao.findRoleById(roleId);
        List<Integer> perIds = roleDao.findPerIdByRoleId(roleId);
        for (Integer perId : perIds) {
            Permission permission = permissionDao.findPermissionByPerId(perId);
            role.getPermissionList().add(permission);
        }
        return role;
    }

    @Override
    public void updateRole(RoleVo roleVo) throws Exception {
        //开启事务
        JdbcUtil.beginTransaction();
        try {
            //更新uesr表内容
            roleDao.updateRole(roleVo);
            //删除t_user_perm 表对应的内容
            roleDao.deleteRoleToPermissionByRoleId(roleVo.getRoleId());
            //添加新的内容
            List<Integer> perIds = roleVo.getPerId();
            for(Integer perId:perIds){
                roleDao.addRoleToPermission(roleVo.getRoleId(),perId);
            }
            //提交
            JdbcUtil.commit();

        }catch (Exception e){

            //回滚
            JdbcUtil.rollback();
            throw new Exception(e);
        }
    }


    private void addRoleToPermission(Integer roleId, List<Integer> perIds) throws Exception {
        for (int perId : perIds) {
            roleDao.addRoleToPermission(roleId, perId);
        }
    }

}
