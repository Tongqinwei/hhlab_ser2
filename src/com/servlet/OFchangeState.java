package com.servlet;

import com.beans.orderForm;
import com.dao.abstruct_dao;
import com.dao.orderForm_dao;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/29.
 * 用于订单状态改变
 */
@WebServlet(name = "OFchangeState")
public class OFchangeState extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();
        String retString="failure";

        //取参数
        String newState = request.getParameter("newState");
        String orderid = request.getParameter("orderid");

        JSONArray jsonString=null;
        if ("comfirm".equals(newState)){
            if (orderForm_dao.stateChange_noConfirm2comfirm(orderid)) retString="success change to "+newState+".";
        }else if ("pay".equals(newState)){
            if (orderForm_dao.stateChange_confirm2pay(orderid)) retString="success change to "+newState+".";
        }else if ("return".equals(newState)){
            if (orderForm_dao.stateChange_pay2finish(orderid)) retString="success change to "+newState+".";
        }else if ("failure".equals(newState)){
            if (orderForm_dao.stateChange_2failure(orderid)) retString="success change to "+newState+".";
        }else {
            retString= String.format("Can not find the newState %s.", newState);
        }

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
