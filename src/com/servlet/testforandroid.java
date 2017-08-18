package com.servlet;

import java.io.IOException;

import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;


/**
 * Created by hasee on 2017/8/9.
 */
@WebServlet(name = "testforandroid")
public class testforandroid extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        String latitude = request.getParameter("latitude");
        System.out.println(latitude);
        String lontitude = request.getParameter("lontitude");


        //JSONArray book_json=null;

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();
        //assert book_json != null;
        //out.write(book_json.toString());
        out.write(String.format("latitude:%s\nlontitude:%s", latitude, lontitude));

        out.flush();
        out.close();
        response.flushBuffer();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
