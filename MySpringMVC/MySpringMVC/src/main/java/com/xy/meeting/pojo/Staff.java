package com.xy.meeting.pojo;

import lombok.Data;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/26
 * Description:
 * Version:V1.0
 */
@Data
public class Staff {
    private int staffId;
    private String staffName;
    private String staffDes;
    private Delegation delegation;
}
