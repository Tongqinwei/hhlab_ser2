package com.resident.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import static java.lang.Thread.sleep;

/**
 * Created by hasee on 2017/7/5.
 */
public class resident extends HttpServlet{
    @Override
    public void init() throws ServletException {
        super.init();

        //自动清除
        autofailureOrderForm AutofailureOF = new autofailureOrderForm();
        AutofailureOF.start();


    }
}
