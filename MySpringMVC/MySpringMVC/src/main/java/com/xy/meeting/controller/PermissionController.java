package com.xy.meeting.controller;

import com.xy.meeting.pojo.Permission;
import com.xy.meeting.response.ResponseCode;
import com.xy.meeting.service.PermissionService;
import com.xy.meeting.utils.ExceptionUtil;
import com.xy.springmvc.annotation.MyAutoWired;
import com.xy.springmvc.annotation.MyController;
import com.xy.springmvc.annotation.MyRequestMapping;
import com.xy.springmvc.annotation.MyResponseBody;
import com.xy.springmvc.response.ModelAndView;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/26
 * Description:
 * Version:V1.0
 */
@MyController
public class PermissionController {
    @MyAutoWired
    PermissionService permissionService;


    /**
     * 查询所有权限
     *
     * @return
     */
    @MyRequestMapping("/permissions/findAll")
    @MyResponseBody
    public ModelAndView findAll() {
        ModelAndView mv = new ModelAndView();
        try {
            List<Permission> permissionList = permissionService.findAll();
            mv.setRc(ResponseCode.SUCCESS);
            mv.setObj(permissionList);
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }

        return mv;
    }


    /**
     * 根据perId删除权限
     *
     * @param perId
     * @return
     */
    @MyRequestMapping("/permissions/deletePermissionByPerId")
    @MyResponseBody
    public ModelAndView deletePermissionByPerId(Integer perId) {
        ModelAndView mv = new ModelAndView();
        try {
            permissionService.deletePermissionByPerId(perId);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/permListNew.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

    /**
     * 添加权限
     *
     * @param permission
     * @return
     */
    @MyRequestMapping("/permissions/addPermission")
    @MyResponseBody
    public ModelAndView addPermission(Permission permission) {
        ModelAndView mv = new ModelAndView();
        try {
            permissionService.addPermission(permission);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/permListNew.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }

        return mv;
    }

    @MyRequestMapping("/permissions/findPermByPerId")
    @MyResponseBody
    public ModelAndView<Permission> findPermByPerId(Integer perId) {
        ModelAndView<Permission> mv = new ModelAndView<>();
        try {
            Permission permission = permissionService.findPermByPerId(perId);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setObj(permission);
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }
    @MyRequestMapping("/permissions/updatePermission")
    @MyResponseBody
    public ModelAndView<Permission> updatePermission(Permission permission) {
        ModelAndView<Permission> mv = new ModelAndView<>();
        try {
            permissionService.updatePermission(permission);
            mv.setRc(ResponseCode.SUCCESS);
            mv.setView("/html/permListNew.html");
        } catch (Exception e) {
            ExceptionUtil.sqlExceptionChoice(mv, e);
        }
        return mv;
    }

}
