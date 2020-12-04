package com.xy.meeting.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/21
 * Description:
 * Version:V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer uId;   //用户id
    private String userName;  //用户名字
    private String telPhone;  //电话
    private String password;  //密码
    private String photo;    //头像
    private String userRealName;  //名字
    private String token;    //token
    private long expireTime;   //过期时间
    private Date createTime;   //创建时间
    private int gender;        //性别
    private Date lastLoginTime; //最后一次的登陆时间
    private List<Staff> staffList = new ArrayList<>();
}
