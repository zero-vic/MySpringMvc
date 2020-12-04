package com.xy.meeting.service;

import com.xy.meeting.pojo.Staff;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/27
 * Description:
 * Version:V1.0
 */
public interface StaffService {
    List<Staff> findAll() throws Exception;
}
