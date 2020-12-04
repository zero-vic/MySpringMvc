package com.xy.meeting.utils;

import com.xy.meeting.response.ResponseCode;
import com.xy.springmvc.response.ModelAndView;

import java.sql.SQLException;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/25
 * Description:封装捕获的异常 返给前端的状态码
 *
 * Version:V1.0
 */
public class ExceptionUtil {

    public static void sqlExceptionChoice(ModelAndView mv ,Exception e){
        e.printStackTrace();
        if(e instanceof SQLException){
            mv.setRc(ResponseCode.SQLEXCEPTION);
        }else {
            mv.setRc(ResponseCode.SYSTEMEXCEPTION);
        }
    }

}
