package com.xy.meeting.dao.impl;

import com.xy.meeting.dao.StaffDao;
import com.xy.meeting.pojo.Staff;
import com.xy.meeting.utils.JdbcUtil;
import com.xy.springmvc.annotation.MyMapper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/27
 * Description:
 * Version:V1.0
 */
@MyMapper
public class StaffDaoImpl implements StaffDao {
    private QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());

    /**
     * 根据staffIdc 查询职位
     *
     * @param staffId
     * @return
     */
    @Override
    public Staff findByStaffId(Integer staffId) {
        Staff staff = null;
        String sql = "select * from t_staff where staffId = ?";
        try {
            staff = qr.query(sql, new BeanHandler<Staff>(Staff.class), staffId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return staff;
    }

    @Override
    public List<Staff> findAll() {
        String sql = "select * from t_staff";
        List<Staff> staffList = null;
        try {
            staffList = qr.query(sql, new BeanListHandler<Staff>(Staff.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return staffList;
    }
}
