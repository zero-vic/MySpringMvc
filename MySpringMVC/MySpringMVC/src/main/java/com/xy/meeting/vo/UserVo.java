package com.xy.meeting.vo;

import lombok.Data;

/*
    用来接收前端请求的与User相关的数据
 */

@Data
public class UserVo {
    private String userName;//用户名
    private String password;//密码
    private String code;//验证码
}
