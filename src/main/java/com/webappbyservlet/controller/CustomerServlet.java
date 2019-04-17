/**
 * created by Zheng Jiateng on 2019/4/10.
 */
package com.webappbyservlet.controller;

import com.webappbyservlet.model.Customer;
import com.webappbyservlet.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 进入 客户列表 界面
 */
@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    // 在init()方法中进行初始化 避免创建多个CustomerService实例
    public void init() throws ServletException {
        customerService = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> customerList = customerService.getCustomerList();
        req.setAttribute("customerList", customerList);
        // 用户无法通过浏览器地址栏直接请求放在内部的JSP，必须通过Servlet程序进行转发（forward）或重定向（redirect）
        //将List对象放入请求属性中 通过请求对象转发到customer.jsp视图
        req.getRequestDispatcher("/WEB-INF/view/customer.jsp").forward(req, resp);
    }
}
