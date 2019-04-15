/**
 * created by Zheng Jiateng on 2019/4/15.
 */
package com.swf4j.chapter2.helper;

import com.swf4j.chapter2.util.PropertiesUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库操作助手类
 */
public final class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    // 使用Apache DBCP的BasicDataSource来获取数据库连接 保证对象是静态的
    private static final BasicDataSource DATA_SOURCE;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();

        QUERY_RUNNER = new QueryRunner();

        String drive = PropertiesUtil.getProperty("jdbc.driver");
        String url = PropertiesUtil.getProperty("jdbc.url");
        String username = PropertiesUtil.getProperty("jdbc.username");
        String password = PropertiesUtil.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        // 通过设置driver, url, username, password来初始化BasicDataSource，并调用其getConnection方法即可获取数据库连接
        DATA_SOURCE.setDriverClassName(drive);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }



}
