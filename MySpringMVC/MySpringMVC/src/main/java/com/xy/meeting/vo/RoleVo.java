package com.xy.meeting.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/26
 * Description:
 * Version:V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    private Integer roleId;
    private String roleName;
    private String roleDescription;
    private List<Integer> perId;

}
