package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.book_brief;
import com.beans.book_brw;
import com.beans.orderForm;
import com.beans.storage_book;
import com.dao.abstruct_dao;
import com.dao.cart_dao;
import com.dao.orderForm_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;
import com.util.OF_util;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/27.
 * 用于创建订单
 */
@WebServlet(name = "makeOrderForm")
public class makeOrderForm  extends HttpServlet {
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
        String barcode1 = request.getParameter("barcode1");
        String barcode2 = request.getParameter("barcode2");
        String unionid = request.getParameter("unionid");

        int userid = user_dao.getUserByUnionId(unionid).getUserid();

        //book_brief[] books= cart_dao.getbooks(userid);
        //JSONArray book_brief_json = JSONArray.fromObject(books);
        //retString = book_brief_json.toString();
        if (barcode1!=null){
            //至少有一本书
            String orderid= OF_util.getNewOrderid();
            OF_util of_util =  new OF_util(unionid,orderid);
            book_brw book1 = new book_brw();
            book1.setBarcode(barcode1);
            of_util.add(book1);
            if (barcode2!=null){
                book_brw book2 = new book_brw();
                book2.setBarcode(barcode2);
                of_util.add(book2);
            }
            orderForm OrderForm = of_util.toOrderForm();
            orderForm_dao OFD = new orderForm_dao(OrderForm);
            if (!OFD.add(true)) retString="create order form failure";
            else retString=orderid;
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
        String barcode1 = null;
        String barcode2 = null;
        String session_id = null;

        try {
            //jsonObject.get("barcode1").getAsString();
            //barcode2 = jsonObject.get("barcode2").getAsString();
            if (jsonObject.get("barcode1")!=null) barcode1=jsonObject.get("barcode1").getAsString();
            if (jsonObject.get("barcode2")!=null) barcode2=jsonObject.get("barcode2").getAsString();
            session_id = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            int j=1;
            out.close();
            response.flushBuffer();
            return;
        }
        unionid = SessionManager.getInstance().getUser(session_id).getOpenID();

        int userid = user_dao.getUserByUnionId(unionid).getUserid();

        //book_brief[] books= cart_dao.getbooks(userid);
        //JSONArray book_brief_json = JSONArray.fromObject(books);
        //retString = book_brief_json.toString();
        if (barcode1!=null){
            //至少有一本书
            String orderid= OF_util.getNewOrderid();
            OF_util of_util =  new OF_util(unionid,orderid);
            book_brw book1 = new book_brw();
            book1.setBarcode(barcode1);
            of_util.add(book1);
            int d123124;
            if (barcode2!=null){
                book_brw book2 = new book_brw();
                book2.setBarcode(barcode2);
                of_util.add(book2);
            }
            orderForm OrderForm = of_util.toOrderForm();
            orderForm_dao OFD = new orderForm_dao(OrderForm);
            if (!OFD.add(true)) retString="create order form failure";
            else retString=orderid;
        }

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
