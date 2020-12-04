package com.xy.meeting.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/27
 * Description:
 * Version:V1.0
 */
@Data
public class UserStaffVo {
    private Integer uId;   //用户id
    private String userName;  //用户名字
    private String telPhone;  //电话
    private String password;  //密码
    private String userRealName;  //名字
    private int gender;        //性别
    private List<Integer> staffIds;

}
