package com.swf4j.chapter2.service;

import com.swf4j.chapter2.model.Customer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomerService Tester.
 *
 * @author <Authors name>
 * @since <pre>04/10/2019</pre>
 * @version 1.0
 */
public class CustomerServiceTest {

    private final CustomerService customerService;

    public CustomerServiceTest() {
        customerService = new CustomerService();
    }

    @Before
    public void init() throws Exception {
        //todo 初始化数据库
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     * Method: getCustomerList(String keyword)
     *
     */
    @Test
    public void testGetCustomerList() throws Exception {
        List<Customer> customerList = customerService.getCustomerList();
        Assert.assertEquals(2, customerList.size());
    }

    /**
     *
     * Method: getCustomer(long id)
     *
     */
    @Test
    public void testGetCustomer() throws Exception {
        long id = 1;
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }

    /**
     *
     * Method: createCustomer(Map<String, Object> fieldMap)
     *
     */
    @Test
    public void testCreateCustomer() throws Exception {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name", "customer100");
        fieldMap.put("contact", "John");
        fieldMap.put("telephone", "13512345678");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }

    /**
     *
     * Method: updateCustomer(long id, Map<String, Object> fieldMap)
     *
     */
    @Test
    public void testUpdateCustomer() throws Exception {
        long id = 1;
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("contact", "Eric");
        boolean result = customerService.updateCustomer(id, fieldMap);
        Assert.assertTrue(result);
    }

    /**
     *
     * Method: deleteCustomer(long id)
     *
     */
    @Test
    public void testDeleteCustomer() throws Exception {
        long id = 1;
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }
} 
