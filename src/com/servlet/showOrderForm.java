package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.orderForm;
import com.dao.abstruct_dao;
import com.dao.orderForm_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;
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
 */
@WebServlet(name = "showOrderForm")
public class showOrderForm extends HttpServlet {
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
        String mode = request.getParameter("mode");//1为按照订单号查询，2为按照用户查询
        String orderid = request.getParameter("orderid");
        String unionid = request.getParameter("unionid");
        orderForm OrderForm =null;
        orderForm[] OrderForms =null;

        JSONArray jsonString=null;
        if ("1".equals(mode)){
            OrderForm= orderForm_dao.getOrderFormByOrderid(orderid);
            jsonString= JSONArray.fromObject(OrderForm);
            retString=jsonString.toString();
        }else if ("2".equals(mode)){
            OrderForms= orderForm_dao.getOrderFormsByUnionid(unionid);
            jsonString= JSONArray.fromObject(OrderForms);
            retString=jsonString.toString();
        }else {
            retString="the mode is error";
        }

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();
        String retString="failure";

        //取参数
        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
        String unionid = null;
        String mode = null;
        String orderid = null;
        String session_id = null;

        try {
            mode = jsonObject.get("mode").getAsString();
            if ("1".equals(mode))orderid = jsonObject.get("orderid").getAsString();
            session_id = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }
        unionid = SessionManager.getInstance().getUser(session_id).getOpenID();

        orderForm OrderForm =null;
        orderForm[] OrderForms =null;

        JSONArray jsonString=null;
        if ("1".equals(mode)){
            OrderForm= orderForm_dao.getOrderFormByOrderid(orderid);
            jsonString= JSONArray.fromObject(OrderForm);
            retString=jsonString.toString();
        }else if ("2".equals(mode)){
            int aa123sdfkjsalifoe2;
            OrderForms= orderForm_dao.getOrderFormsByUnionid(unionid);
            jsonString= JSONArray.fromObject(OrderForms);
            retString=jsonString.toString();
        }else {
            retString="the mode is error";
        }

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
