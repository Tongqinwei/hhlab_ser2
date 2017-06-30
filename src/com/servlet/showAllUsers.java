package com.servlet;

import com.beans.user_brief;
import com.dao.abstruct_dao;
import com.dao.book_dao;
import com.dao.orderForm_dao;
import com.dao.user_dao;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/30.
 */
@WebServlet(name = "importFile")
public class showAllUsers {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();
        int _begin=1;
        int _end=20;
        if (request.getParameter("begin")!=null) _begin= Integer.parseInt( request.getParameter("begin"));
        if (request.getParameter("end")!=null) _end= Integer.parseInt( request.getParameter("end"));
        user_brief[] user_briefs = user_dao.getAllUser_bf(_begin,_end);
        JSONArray jsonString= JSONArray.fromObject(user_briefs);
        String retString=jsonString.toString();
        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
