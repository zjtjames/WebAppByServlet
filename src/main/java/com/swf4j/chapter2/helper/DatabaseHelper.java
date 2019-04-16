/**
 * created by Zheng Jiateng on 2019/4/15.
 */
package com.swf4j.chapter2.helper;

import com.swf4j.chapter2.util.PropertiesUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作助手类
 */
public final class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    // 用ThreadLocal存放当前线程中的Connection 可以将ThreadLocal理解为一个隔离线程的容器
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    // 使用DbUtils提供的QueryRunner对象可以面向实体（Entity）进行查询
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
                //从连接池中取一个连接
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

    /**
     * 查询实体列表
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            // DbUtil首先执行sql语句并返回一个ResultSet，随后由ResultSetHandler将ResultSet转换为所需类型对象，
            // 因此要选合适的ResultSetHandler（此例中是BeanListHandler）
            // new泛型类的时候必须要加<>  new BeanListHandler<T>(entityClass)
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        } // 用了DBCP连接池之后就不需要再finally closeConnection();
        return entityList;
    }

    /**
     * 查询单个实体
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {

            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 执行查询语句（更为强大的查询方法）
     * 输入一个sql和动态参数
     * 返回List对象
     * Map表示列名与与列值的映射关系
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String,Object>> result;
        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 执行更新语句（包括update,insert,delete）
     * 返回执行后受影响的行数，也就是说，更新了多少条记录
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 插入实体
     * 把sql拼出来 再调用executeUpdate
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (MapUtils.isEmpty(fieldMap)) {
            LOGGER.error("can't insert entity: fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql = sql + columns + " VALUES " + values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     * 把sql拼出来 再调用executeUpdate
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (MapUtils.isEmpty(fieldMap)) {
            LOGGER.error("can't update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + entityClass.getSimpleName() + " SET ";
        StringBuffer columns = new StringBuffer();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append("=?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), " WHERE id=?");
        sql += columns;
        List<Object> paramList = new ArrayList<>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 删除实体
     * 把sql拼出来 再调用executeUpdate
     */



    /**
     * "".getClass().getName() java.lang.String
     * "".getClass().getSimpleName()
     * 此方法叫getTableName是因为entityClass是model的类型，也就是pojo的类型，而pojo的类型与表名相同
     */
    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

}
