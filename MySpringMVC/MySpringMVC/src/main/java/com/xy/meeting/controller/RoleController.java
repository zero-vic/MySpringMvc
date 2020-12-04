package com.xy.meeting.controller;

import com.xy.meeting.pojo.Role;
import com.xy.meeting.response.ResponseCode;
import com.xy.meeting.service.RoleService;
import com.xy.meeting.utils.ExceptionUtil;
import com.xy.meeting.vo.RoleVo;
import com.xy.springmvc.annotation.*;
import com.xy.springmvc.response.ModelAndView;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/25
 * Description:
 * Version:V1.0
 */
@MyController
public class RoleController {
    @MyAutoWired
    RoleService roleService;

    /**
     * 查询所有角色信息
     *
     * @return
     * @throws Exception
     */
    @MyRequestMapping("/roles/findAll")
    @MyResponseBody
    public ModelAndView<Role> findAll() throws Exception {
        ModelAndView mv = new ModelAndView();
        try {
            List<Role> roleList = roleService.findAll();
            mv.setRc(ResponseCode.SUCCESS);
            mv.setObj(roleList);
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

    /**
     * * 根据id删除role
     * * roleid是permission的外键
     * * 有点问题
     * * @param roleId
     *
     * @param roleId
     * @return
     */
    @MyRequestMapping("/roles/deleteRoleByRoleId")
    @MyResponseBody
    public ModelAndView deleteRoleByRoleId(Integer roleId) {
        ModelAndView mv = new ModelAndView();

        try {
            roleService.deleteRoleByRoleId(roleId);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/roleList.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }

        return mv;
    }

    /**
     * 添加角色
     *
     * @param roleVo
     * @return
     */
    @MyRequestMapping("/roles/addRole")
    @MyResponseBody
    public ModelAndView addRole(@MyRequestBody RoleVo roleVo) {
        ModelAndView mv = new ModelAndView();
        try {
            roleService.addRole(roleVo);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/roleList.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

    /**
     * 根据roleId查询 role
     *
     * @param roleId
     * @return
     */
    @MyRequestMapping("/roles/findRoleById")
    @MyResponseBody
    public ModelAndView<Role> findRoleById(Integer roleId) {
        ModelAndView<Role> mv = new ModelAndView();
        try {
            Role role = roleService.findRoleById(roleId);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setObj(role);
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }

        return mv;
    }

    @MyRequestMapping("/roles/updateRole")
    @MyResponseBody
    public ModelAndView updateRole(@MyRequestBody RoleVo roleVo) {
        ModelAndView mv = new ModelAndView();
        try {
            roleService.updateRole(roleVo);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/roleList.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }

        return mv;
    }

}
