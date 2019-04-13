/**
 * created by Zheng Jiateng on 2019/4/10.
 */
package com.swf4j.chapter2.service;

import com.swf4j.chapter2.model.Customer;
import com.swf4j.chapter2.util.PropertiesUtil;

import java.util.List;
import java.util.Map;

/**
 * 服务层 业务逻辑
 */
public class CustomerService {

    //JDBC相关的配置项
    private static final String DRIVER = PropertiesUtil.getProperty("jdbc.driver");
    private static final String URL = PropertiesUtil.getProperty("jdbc.url");
    private static final String USERNAME = PropertiesUtil.getProperty("jdbc.username");
    private static final String PASSWORD = PropertiesUtil.getProperty("jdbc.password");

    /**
     * 获取客户列表
     */
    public List<Customer> getCustomerList() {
        "".getClass();
        //todo
        return null;
    }

    /**
     * 获取客户
     */
    public Customer getCustomer(long id) {
        //todo
        return null;
    }

    /**
     * 创建客户
     */
    public boolean createCustomer(Map<String, Object> fieldMap) {
        //todo
        return false;
    }

    /**
     * 更新客户
     */
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        //todo
        return false;
    }

    /**
     * 删除客户
     */
    public boolean deleteCustomer(long id) {
        //todo
        return false;
    }
}
