package com.resident.servlet;

import com.dao.orderForm_dao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import static java.lang.Thread.sleep;

/**
 * Created by hasee on 2017/7/5.
 */
public class autoFailureOF extends HttpServlet{
    @Override
    public void init() throws ServletException {
        super.init();
        while (true){
            orderForm_dao.autofailure(20);
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                System.err.println("can not sleep");
                e.printStackTrace();
            }
        }
    }
}
