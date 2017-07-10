package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.beans.book_brw;
import com.beans.orderForm;
import com.dao.abstruct_dao;
import com.dao.book_dao;
import com.dao.orderForm_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;
import com.util.OF_util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/30.
 * 用于导入文件
 */
@WebServlet(name = "importFile")
public class importFile extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();
        String retString="{\"success\":false}";

        //取参数
        String filename = request.getParameter("filename");
        String OS = request.getParameter("OS");

        book_dao.importFile(OS,filename);

        retString="{\"success\":true}";
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
        String retString="{\"success\":false}";

        //取参数
        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
        String filename = null;
        String OS =null;
        try {
            if (jsonObject.get("filename")!=null) filename=jsonObject.get("filename").getAsString();
            if (jsonObject.get("OS")!=null) OS=jsonObject.get("OS").getAsString();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            int j=1;
            out.close();
            response.flushBuffer();
            return;
        }
        book_dao.importFile(OS,filename);


        retString="{\"success\":true}";
        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
