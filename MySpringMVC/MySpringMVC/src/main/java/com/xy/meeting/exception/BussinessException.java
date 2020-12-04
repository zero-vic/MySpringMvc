package com.xy.meeting.exception;


import com.xy.meeting.response.ResponseCode;
import lombok.Getter;

/**
 * 处理业务逻辑中的异常
 */
@Getter
public class BussinessException extends Exception {

    private int code;
    private String msg;
    public BussinessException(ResponseCode rc) {
        super(rc.getMsg());
        this.code = rc.getCode();
        this.msg = rc.getMsg();
    }


}
