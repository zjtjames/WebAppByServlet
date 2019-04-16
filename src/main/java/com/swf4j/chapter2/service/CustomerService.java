/**
 * created by Zheng Jiateng on 2019/4/10.
 */
package com.swf4j.chapter2.service;

import com.swf4j.chapter2.helper.DatabaseHelper;
import com.swf4j.chapter2.model.Customer;
import com.swf4j.chapter2.util.PropertiesUtil;

import java.util.List;
import java.util.Map;

/**
 * 服务层 业务逻辑
 */
public class CustomerService {

    /**
     * 获取客户列表
     *
     */


    public List<Customer> getCustomerList() {
        String sql = "SELECT * FROM customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql);
    }

    /**
     * 获取客户
     */
    public Customer getCustomer(long id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        return DatabaseHelper.queryEntity(Customer.class, sql, id);
    }

    /**
     * 创建客户
     */
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    /**
     * 更新客户
     */
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    /**
     * 删除客户
     */
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
