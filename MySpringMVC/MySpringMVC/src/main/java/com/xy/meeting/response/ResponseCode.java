package com.xy.meeting.response;

import com.xy.springmvc.response.ResponseCodeInterface;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/23
 * Description:
 * Version:V1.0
 */
public enum ResponseCode implements ResponseCodeInterface {
    SUCCESS(100, "请求成功"),
    VERIFYEXCEPTION(101, "验证码错误"),
    USERNAMEEXCEPTION(102, "用户名错误"),
    PASSWORDEXCEPTION(103, "密码错误"),
    SQLEXCEPTION(104, "SQL异常"),
    SYSTEMEXCEPTION(105, "系统异常"),
    MAILEXCEPTION(106,"邮件错误");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
