package com.xy.meeting.controller;

import com.xy.meeting.pojo.Staff;
import com.xy.meeting.response.ResponseCode;
import com.xy.meeting.service.StaffService;
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
 * Date:2020/11/27
 * Description:
 * Version:V1.0
 */
@MyController
public class StaffController {
    @MyAutoWired
    StaffService staffService;

    /**
     * 查询所有职位
     * @return
     */
    @MyRequestMapping("/staffs/findAll")
    @MyResponseBody
    public ModelAndView findAll(){
        ModelAndView mv = new ModelAndView();
        try{
            List<Staff> staffList = staffService.findAll();
            mv.setRc(ResponseCode.SUCCESS);
            mv.setObj(staffList);
        }catch (Exception e){
            ExceptionUtil.sqlExceptionChoice(mv,e);
        }
        return mv;
    }
}
