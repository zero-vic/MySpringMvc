package com.xy.meeting.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/25
 * Description:角色实体类
 * Version:V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private int roleId;
    private String roleName;
    private String roleDescription;
    private List<Permission> permissionList = new ArrayList<>();
}
