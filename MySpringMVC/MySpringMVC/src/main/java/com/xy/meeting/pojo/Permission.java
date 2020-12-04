package com.xy.meeting.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/25
 * Description:权限表实体类
 * Version:V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private int perId;
    private String perName;
    private String perDes;
}
