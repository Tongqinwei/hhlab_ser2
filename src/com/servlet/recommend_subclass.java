package com.servlet;

import com.beans.book;
import com.dao.abstruct_dao;
import com.dao.book_dao;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/30.
 * 用于每本书的推荐
 */
@WebServlet(name = "recommend_subclass")
public class recommend_subclass extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        abstruct_dao.connect();
        String subclass=request.getParameter("subclass");
        book[] Books = book_dao.searchBySubclass(subclass,1,5);
        abstruct_dao.close();
        JSONArray book_json = JSONArray.fromObject(Books);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();
        out.write("{\"message\":"+book_json.toString()+"}");
        out.flush();
        out.close();
        response.flushBuffer();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
