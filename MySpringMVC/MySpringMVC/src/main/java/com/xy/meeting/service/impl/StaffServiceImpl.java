package com.xy.meeting.service.impl;

import com.xy.meeting.dao.StaffDao;
import com.xy.meeting.pojo.Staff;
import com.xy.meeting.service.StaffService;
import com.xy.springmvc.annotation.MyAutoWired;
import com.xy.springmvc.annotation.MyService;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/27
 * Description:
 * Version:V1.0
 */
@MyService
public class StaffServiceImpl implements StaffService {
    @MyAutoWired
    StaffDao staffDao;


    @Override
    public List<Staff> findAll() throws Exception {
        return staffDao.findAll();
    }
}
