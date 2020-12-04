package com.xy.meeting.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/23
 * Description:
 * Version:V1.0
 */
public class JdbcUtil {
    private static DataSource dataSource = new ComboPooledDataSource();

    /**
     * 解决线程安全问题
     */
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();


    /**
     * 获取c3p0连接池
     *
     * @return
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 获取当前线程自己的connection对象
     *
     * @return
     * @throws SQLException
     */
    public static Connection getTranConnection() throws SQLException {
        Connection connection = tl.get();
        if (connection == null) {
            connection = dataSource.getConnection();
            tl.set(connection);
        }
        return connection;
    }

    /**
     * 返回连接池的connection对象
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 开启事务
     *
     * @throws SQLException
     */
    public static void beginTransaction() throws SQLException {
        Connection conn = tl.get();
        if (conn != null) {
            throw new SQLException("事务不要重复开启");
        }
        conn = getTranConnection();
        conn.setAutoCommit(false);
    }

    /**
     * 提交事务
     *
     * @throws SQLException
     */
    public static void commit() throws SQLException {
        Connection conn = tl.get();
        if (conn == null) {
            throw new SQLException("事务没有开启，不能提交");
        }
        conn.commit();
        conn.close();
        tl.remove();

    }

    /**
     * 回滚事务
     *
     * @throws SQLException
     */
    public static void rollback() throws SQLException {
        Connection conn = tl.get();
        if (conn == null) {
            throw new SQLException("事务没有开启，不能回滚！");
        }
        conn.rollback();
        conn.close();
        tl.remove();
    }


}
