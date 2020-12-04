package com.xy.meeting.utils;

import java.lang.reflect.Field;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/16
 * Description:
 * Version:V1.0
 */
public class MyUpdateSql {
    /**
     * UPDATE t_customer set cname = ?, gender = ?,birthday = ? ,cellphone = ?,email = ?, description = ? where cid = ?
     */

    /**
     * 根据主键 更新 表的SQL语句生成
     * 限制条件：主键必须是bean 的第一个属性
     * @param clazz
     * @param tableName
     * @param <T>
     */
    public static <T> String updateSql(Class<T> clazz, String tableName) {
        Field[] fields = clazz.getDeclaredFields();
        String sql = "UPDATE " + tableName + " SET ";
        StringBuilder sb = new StringBuilder(sql);
        for (int i = 1; i < fields.length; i++) {
            if (i == 1) {
                sb.append(fields[i].toString().substring(fields[i].toString().lastIndexOf(".") + 1)).append(" = ? ");
            } else {
                sb.append(",").append(fields[i].toString().substring(fields[i].toString().lastIndexOf(".") + 1)).append(" = ? ");
            }
        }
        sb.append(" WHERE ").append(fields[0].toString().substring(fields[0].toString().lastIndexOf(".") + 1)).append(" = ? ");
        return sb.toString();
    }

}
