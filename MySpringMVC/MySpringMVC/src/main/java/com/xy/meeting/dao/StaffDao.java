package com.xy.meeting.dao;

import com.xy.meeting.pojo.Staff;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/27
 * Description:
 * Version:V1.0
 */
public interface StaffDao {
    Staff findByStaffId(Integer staffId);

    List<Staff> findAll();
}
